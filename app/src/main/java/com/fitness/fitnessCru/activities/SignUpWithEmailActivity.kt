package com.fitness.fitnessCru.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivitySignupWithEmailBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.SignUpWithEmailResponse
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.EmailLoginViewModel
import com.fitness.fitnessCru.viewmodel.SignUpWithEmailViewModel
import com.fitness.fitnessCru.vm_factory.EmailLoginVMFactory
import com.fitness.fitnessCru.vm_factory.SignUpWithEmailVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.king.keyboard.KingKeyboard
import com.orhanobut.hawk.Hawk
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity__signup_with_email.*

class SignUpWithEmailActivity : AppCompatActivity() {

    private lateinit var signUpWithEmailBinding: ActivitySignupWithEmailBinding
    private lateinit var loading: CustomProgressLoading
    private lateinit var user: QBUser

    private lateinit var kingKeyboard: KingKeyboard

    private lateinit var viewModel: SignUpWithEmailViewModel

    private var beforeCount = 0

    private val deviceType = 0
    lateinit var deviceToken: String
    private var email = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpWithEmailBinding = ActivitySignupWithEmailBinding.inflate(
            layoutInflater
        )
        setContentView(signUpWithEmailBinding!!.root)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    deviceToken = token.toString()
                    Hawk.put(DEVICE_TOKEN, deviceToken)


                } else {
                    Log.v(
                        "LoginMainActivity",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    deviceToken = ""
                }
                Log.v("FCMToken", deviceToken)
            }

        loading = CustomProgressLoading(this)
        keyboard()
        signUpUserWithEmail()
        goBack()

        var userDetail = Session.getUserDetails()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun keyboard() {
        kingKeyboard = KingKeyboard(this, signUpWithEmailBinding!!.keyboardParent)
        kingKeyboard.register(signUpWithEmailBinding!!.emailEt, KingKeyboard.KeyboardType.NORMAL)
        signUpWithEmailBinding!!.emailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeCount = s?.length ?: 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (s?.length) {
                    0 -> {
                        if (kingKeyboard.getKeyboardType() != KingKeyboard.KeyboardType.LICENSE_PLATE_PROVINCE) {
                            kingKeyboard.sendKey(KingKeyboard.KEYCODE_BACK)
                        }
                    }
                    1 -> {
                        if (beforeCount == 0 && kingKeyboard.getKeyboardType() == KingKeyboard.KeyboardType.LICENSE_PLATE_PROVINCE) {
                            kingKeyboard.sendKey(KingKeyboard.KEYCODE_MODE_CHANGE)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        kingKeyboard.setKeyboardCustom(R.xml.keyboard_custom)
        kingKeyboard.setBackground(ColorDrawable(Color.parseColor("#1C1C1C")))
    }

    override fun onBackPressed() {
        if (kingKeyboard.isShow()) {
            kingKeyboard.hide()
            return
        }
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        kingKeyboard.onResume()
        (signUpWithEmailBinding.emailEt as TextView).text = email
    }

    override fun onDestroy() {
        super.onDestroy()
        kingKeyboard.onDestroy()
    }

    private fun emailValidation(): Boolean {

        email = signUpWithEmailBinding.emailEt.text.toString().trim()
        if (email.isEmpty()) {
            signUpWithEmailBinding.emailEt.error =
                resources.getString(R.string.please_enter_email)
            return false
        } else if (!Utils.validEmail(email)) {
            signUpWithEmailBinding.emailEt.error =
                resources.getString(R.string.please_enter_correct_email)
            return false
        }
        return true
    }

    private fun signUpUserWithEmail() {
        val repository = Repository()
        val viewModelFactory = SignUpWithEmailVMFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            SignUpWithEmailViewModel::class.java
        )
        signUpWithEmailBinding.proceedBtn.setOnClickListener {
            if (emailValidation()) {
                loading.showProgress()
                quickbloxlogin()
                // viewModel.signUpWithEmail(email_et.text.toString(), deviceType)


            }
        }
        viewModel.response.observe(this) {
            loading.hideProgress()
            val signUpUser = it.body()
            try {
                if (signUpUser != null && it.isSuccessful && signUpUser.error_code ?: 1 == 0) {
                    Session.destroySession()

                    Session.setToken(signUpUser.data.access_token)

                    //  Hawk.put(ACCESS_TOKEN, it.body()!!.data.access_token)


                    callSaveFcm()
                    Session.setUserDetails(signUpUser.data)

                    if (it.code() == 201) {

                        /* Toast.makeText(
                             this,
                             "Enter otp: " + signUpUser.data.email_verification_code,
                             Toast.LENGTH_LONG
                         ).show()*/

                        val intent = Intent(this, EmailVerificationActivity::class.java)
                        var bundle = Bundle()
                        bundle.putString(
                            "email_verification_code",
                            signUpUser.data.email_verification_code
                        )
                        bundle.putString(
                            "way",
                            "sign"
                        )
                        bundle.putString("userEmail", signUpUser.data.email)
                        bundle.putString("statusCode", it.code().toString())
                        bundle.putString("path", "email")
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else if (it.code() == 200) {
                        val intent = Intent(this, LoginMainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    if (it.code() == 400 || it.code() == 401) {
                        val error =
                            Util.error(it.errorBody(), SignUpWithEmailResponse::class.java)
                        Toast.makeText(
                            this,
                            error.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun callSaveFcm() {
        val repository by lazy { Repository() }
        val factory by lazy { EmailLoginVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[EmailLoginViewModel::class.java]
        }
        // loading.showProgress()
        viewModel.saveFcmToken(this, applicationContext)
    }

    private fun goBack() {
        signUpWithEmailBinding.gobackbtn.setOnClickListener {
            val goBack = Intent(this, MainActivity::class.java)
            startActivity(goBack)
        }
    }


    private fun quickbloxlogin() {
        val user = createUserWithEnteredData()
        signUpNewUser(user)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        // val userLogin =  numberBinding.moNumber?.text.toString().trim()
        val userEmail = signUpWithEmailBinding.emailEt.text.toString().trim()
        qbUser.login = userEmail
        qbUser.fullName = userEmail

        Session.setEmailForCreate(userEmail)

        //qbUser.fullName = userFullName
        qbUser.password = userEmail
        return qbUser
    }

    private fun signUpNewUser(newUser: QBUser) {
        // loading.hideProgress()
        //  loading.showProgress()
        signUp(newUser, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                SharedPrefsHelper.saveQbUser(newUser)
                loginToChat(result)
            }

            override fun onError(e: QBResponseException) {
                if (e.httpStatusCode == ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                    signInCreatedUser(newUser)
                } else {
                    loading.hideProgress()
                    longToast(R.string.sign_up_error)
                }
            }
        })
    }

    private fun signInCreatedUser(user: QBUser) {
        signInUser(user, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                SharedPrefsHelper.saveQbUser(user)
                updateUserOnServer(user)
            }

            override fun onError(responseException: QBResponseException) {
                loading.hideProgress()
                longToast(R.string.sign_in_error)
            }
        })
    }


    private fun updateUserOnServer(user: QBUser) {
        user.password = null
        QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(updUser: QBUser?, params: Bundle?) {
                // loading.hideProgress()
                /*  OpponentsActivity.start(this@SignUpWithEmailActivity)
                 finish()
                 if (cb_login.isChecked)
                     sharedPreference.isLogin = true
                 mailto:startactivity(intent(this@loginactivity,homeactivity::class.java))
                 finishAffinity()*/
                //showLoginUpdateDetailsDialog()


                viewModel.signUpWithEmail(email_et.text.toString(), deviceType)
                //viewModel.sendLoginRequest( numberBinding!!.moNumber.text.toString().trim { it <= ' ' }, deviceToken, deviceId, deviceType)
                // loading.showProgress()


            }

            override fun onError(responseException: QBResponseException?) {
                loading.hideProgress()
                longToast(R.string.update_user_error)
            }
        })
    }


    private fun loginToChat(qbUser: QBUser) {
        val userEmail = Session.getEmailForCreate()
        // qbUser.password = userEmail
        qbUser.password = userEmail
        user = qbUser
        startLoginService(qbUser)
    }

    private fun startLoginService(qbUser: QBUser) {
        val tempIntent = Intent(this, LoginService::class.java)
        val pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
        LoginService.start(this, qbUser, pendingIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == EXTRA_LOGIN_RESULT_CODE) {
            //  loading.hideProgress()

            var isLoginSuccess = false
            data?.let {
                isLoginSuccess = it.getBooleanExtra(EXTRA_LOGIN_RESULT, false)
            }

            var errorMessage = getString(R.string.unknown_error)
            data?.let {
                errorMessage = it.getStringExtra(EXTRA_LOGIN_ERROR_MESSAGE) ?: ""
            }

            if (isLoginSuccess) {
                SharedPrefsHelper.saveQbUser(user)
                signInCreatedUser(user)
            } else {
                longToast(getString(R.string.login_chat_login_error) + errorMessage)
            }
        }
    }

}