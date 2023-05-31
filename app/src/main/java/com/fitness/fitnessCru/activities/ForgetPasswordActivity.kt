package com.fitness.fitnessCru.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityForgetPasswordBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ForgotPasswordResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.ForgotPasswordViewModel
import com.fitness.fitnessCru.vm_factory.ForgotPasswordVMFactory
import com.king.keyboard.KingKeyboard
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var kingKeyboard: KingKeyboard
    private lateinit var loading: CustomProgressLoading

    private var beforeCount = 0

    private lateinit var binding: ActivityForgetPasswordBinding

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var user: QBUser


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ForgetPasswordActivity,
            R.layout.activity_forget_password
        )
        loading = CustomProgressLoading(this)
        keyboard()
        forgotPassword()
        binding.gobackbtn.setOnClickListener { onBackPressed() }
    }

    private fun isValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()
    }

    private fun forgotPassword() {
        val repository = Repository()
        val factory = ForgotPasswordVMFactory(repository)
        viewModel = ViewModelProvider(
            this@ForgetPasswordActivity,
            factory
        )[ForgotPasswordViewModel::class.java]

        binding!!.proceedBtn.setOnClickListener {
            if (isValid()) {
                loading.showProgress()
                quickbloxlogin()
//                viewModel.sendForgotPasswordRequest(binding.email.text.toString())
            } else Toast.makeText(this, "Please Fill Valid Email", Toast.LENGTH_LONG).show()
        }
        viewModel.response.observe(this@ForgetPasswordActivity) {
            loading.hideProgress()
            var userData = it.body()
            try {
                if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {

                    Session.setToken(userData!!.data.access_token)

                    Session.setUserDetails(userData.data)

                    val intent = Intent(this, EmailVerificationActivity::class.java)
                    var bundle = Bundle()
                    bundle.putString(
                        "email_verification_code",
                        userData.data.email_verification_code
                    )
                    bundle.putString(
                        "way",
                        "forgot"
                    )
                    bundle.putString("userEmail", userData.data.email)
                    bundle.putString("statusCode", it.code().toString())
                    bundle.putString("path", "forget")
                    intent.putExtras(bundle)
                    startActivity(intent)
//                    Util.toast(this, userData.data.email_verification_code)
                } else {
                    if (it.code() == 400) {
                        val error =
                            Util.error(it.errorBody(), ForgotPasswordResponse::class.java)
                        Util.toast(this, "Error: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                Util.toast(this, "Error : ${e.message}")
            }
        }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        kingKeyboard.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun keyboard() {
        kingKeyboard = KingKeyboard(this, binding!!.keyboardParent)
        kingKeyboard.register(binding!!.email, KingKeyboard.KeyboardType.NORMAL)
        binding!!.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
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

    private fun quickbloxlogin() {
        val user = createUserWithEnteredData()
        signUpNewUser(user)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        // val userLogin =  numberBinding.moNumber?.text.toString().trim()
        val userEmail = binding.email.text.toString().trim()
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


                viewModel.sendForgotPasswordRequest(binding.email.text.toString())
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