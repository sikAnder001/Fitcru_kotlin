package com.fitness.fitnessCru.activities


import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityMobileNumberBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.MobileLoginResponse
import com.fitness.fitnessCru.response.StoreQuickbloxResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.OnClick
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.EmailLoginViewModel
import com.fitness.fitnessCru.viewmodel.MobileLoginViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.EmailLoginVMFactory
import com.fitness.fitnessCru.vm_factory.MobileLoginVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

class MobileNumberActivity : AppCompatActivity() {

    private lateinit var user: QBUser

    private var numberBinding: ActivityMobileNumberBinding? = null
    private lateinit var loading: CustomProgressLoading

    private lateinit var deviceId: String
    private val deviceType = 0
    lateinit var deviceToken: String
    private lateinit var viewModel: MobileLoginViewModel
    private var mNumber = ""
    var userid: String = ""

    private lateinit var repository: Repository

    private lateinit var factory: MobileLoginVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numberBinding = ActivityMobileNumberBinding.inflate(layoutInflater)
        setContentView(numberBinding!!.root)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    deviceToken = token.toString()
                } else {
                    Log.v(
                        "LoginMainActivity",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    deviceToken = ""
                }
            }

        repository = Repository()

        factory = MobileLoginVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(MobileLoginViewModel::class.java)

        observerStore()



        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        loading = CustomProgressLoading(this)

        numberBinding!!.gobackbtn.setOnClickListener {
            startActivity(
                Intent(this@MobileNumberActivity, LoginMainActivity::class.java)
            )
        }

        keyBoardSetup()

        view()


    }

    private fun keyBoardSetup() {
        numberBinding!!.apply {
            moNumber.setRawInputType(InputType.TYPE_CLASS_TEXT)
            moNumber.showSoftInputOnFocus = false
            val ic: InputConnection = moNumber.onCreateInputConnection(EditorInfo())
            keyboard.call(object : OnClick {
                override fun click() {
                    keyboard.visibility = GONE
                }
            })

            moNumber.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if (!keyboard.isVisible)
                        keyboard.visibility = VISIBLE
                    return true
                }
            })
            keyboard.setInputConnection(ic)
        }
    }

    private fun observerStore() {
        viewModel.responseStoreId.observe(this) {
            val body = it.body()!!

            if (it.isSuccessful && body != null && body.error_code == 0) {

                //  showToast("storedQuickbloxAPI")

            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    applicationContext, it.body()!!.message
                )

            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.toast(
                    applicationContext,
                    Util.error(it.errorBody(), StoreQuickbloxResponse::class.java).message
                )
                loading.hideProgress()
            }
        }
    }

    private fun view() {

        numberBinding!!.proceedBtn.setOnClickListener {

            mNumber =
                numberBinding!!.moNumber.text.toString().trim { it <= ' ' }

            if (mNumber.isEmpty() || mNumber.length < 10 || mNumber.length > 10) {
                loading.hideProgress()
                Toast.makeText(
                    this@MobileNumberActivity,
                    "Please Enter Mobile Number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                loading.showProgress()


                quickbloxlogin()
            }

        }


        //activation response
        viewModel.activateNumber.observe(this) {
            loading.hideProgress()
            val userData = it.body()
            if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {
                Session.destroySession()

                Session.setToken(userData!!.data.access_token)
                Session.setUserDetails(userData.data)

                callSaveFcm()

                numberBinding!!.keyboard.visibility = GONE
                var bundle = Bundle()
                bundle.putString("otp", userData.data.otp)
                bundle.putString("type", "logIn")
                bundle.putString("number", userData.data.phone_number)
                val intent = Intent(
                    this@MobileNumberActivity,
                    OTPVerifyActivity::class.java
                )
                intent.putExtras(bundle)
                startActivity(
                    intent
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    "Error: Something went wrong yess",
                    Toast.LENGTH_LONG
                )
                    .show()
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

    private fun getUserDetail(accessToken: String) {
        val repository by lazy { Repository() }
        val factory by lazy { UserDetailsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[UserDetailsViewModel::class.java]
        }
        // loading.showProgress()
        viewModel.getUserDetails()
        viewModel.response.observe(this@MobileNumberActivity) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                val data = it.body()!!.data
                if (data.name != null || data.email != null)
                    Session.destroySession()
                Session.setUserDetails(data)
                Session.setToken(accessToken)

            } else {
            }
        }
    }

    fun proceed() {


        // viewModel.sendLoginRequest( numberBinding!!.moNumber.text.toString().trim { it <= ' ' }, deviceToken, deviceId, deviceType)

        viewModel.response.observe(this@MobileNumberActivity) {
            loading.hideProgress()
            val userlog = it.body()
            try {
                if (userlog != null && it.isSuccessful && userlog.error_code ?: 1 == 0) {

                    //  Hawk.put(ACCESS_TOKEN, it.body()!!.data.access_token)
                    Session.setToken(userlog.data.access_token)
                    getUserDetail(userlog.data.access_token)

                    callSaveFcm()

                    userid = userlog.data.id.toString()

                    numberBinding!!.keyboard.visibility = GONE
                    if (it.code() == 201) {
                        var bundle = Bundle()
                        bundle.putString("otp", userlog.data.otp)
                        bundle.putString("type", "signUp")
                        bundle.putString("number", userlog.data.phone_number)
                        if (userlog.data.quick_blox_id == null) {
                            storeqbid()
                        }
                        val intent = Intent(
                            this@MobileNumberActivity,
                            OTPVerifyActivity::class.java
                        )
                        intent.putExtras(bundle)
                        startActivity(
                            intent
                        )
                    } else {
                        var bundle = Bundle()
                        bundle.putString("otp", userlog.data.otp)
                        bundle.putString("type", "logIn")
                        bundle.putString("number", userlog.data.phone_number)


                        if (userlog.data.quick_blox_id == null) {
                            storeqbid()
                        }
                        val intent = Intent(
                            this@MobileNumberActivity,
                            OTPVerifyActivity::class.java
                        )
                        intent.putExtras(bundle)
                        startActivity(
                            intent
                        )
                    }

                } else {

                    if (it.code() == 471) {
                        val dialogBuilder =
                            AlertDialog.Builder(
                                this@MobileNumberActivity,
                                R.style.AlertDialogStyle
                            )

                        dialogBuilder.setMessage("Do you want to activate your account?")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Yes",
                                DialogInterface.OnClickListener { dialog, id ->
                                    viewModel.activateNumberAccount(
                                        numberBinding!!.moNumber.text.toString()
                                            .trim { it <= ' ' })
                                    loading.showProgress()
                                })
                            .setNegativeButton(
                                "No",
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })

                        val alert = dialogBuilder.create()
                        alert.setTitle("Activate Account")
                        alert.show()
                    } else if (it.code() == 401) {
                        loading.hideProgress()
                        val error =
                            Util.error(it.errorBody(), MobileLoginResponse::class.java)
                        Toast.makeText(
                            this@MobileNumberActivity,
                            error.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MobileNumberActivity,
                    "Error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.sendLoginRequest(
            numberBinding!!.moNumber.text.toString().trim { it <= ' ' },
            deviceToken,
            deviceId,
            deviceType,
            ""
        )


    }


    private fun quickbloxlogin() {
        //   showToast("quickblox")
        val user = createUserWithEnteredData()
        signUpNewUser(user)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        val userLogin = numberBinding?.moNumber?.text.toString().trim()
        // val userEmail = numberBinding.email.text.toString().trim()
        qbUser.login = userLogin
        qbUser.fullName = userLogin
        qbUser.password = userLogin
        return qbUser
    }

    private fun signUpNewUser(newUser: QBUser) {
        //  loading.hideProgress()
        // loading.showProgress()
        signUp(newUser, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                // longToast("quickblox signup")


                SharedPrefsHelper.saveQbUser(newUser)
                loginToChat(result)
                //  longToast("quickNewUserCreated ID:" + SharedPrefsHelper.getQbUser().id.toString())
                loading.showProgress()
                proceed()

            }

            override fun onError(e: QBResponseException) {
                if (e.httpStatusCode == ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                    signInCreatedUser(newUser)
                } else {
                    loading.hideProgress()
                    //    longToast(R.string.sign_up_error)
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
                //   longToast(R.string.sign_in_error)
            }
        })
    }


    private fun updateUserOnServer(user: QBUser) {
        // longToast("user udated Success block")
        user.password = null
        QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(updUser: QBUser?, params: Bundle?) {

                //  longToast("UpdateUser ID:" + SharedPrefsHelper.getQbUser().id.toString())
                loading.showProgress()
                proceed()
                // loading.hideProgress()
                /*   OpponentsActivity.start(this@LoginActivity)
                  finish()
                  if (cb_login.isChecked)
                      sharedPreference.isLogin = true
                  startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                  finishAffinity()*/
                //showLoginUpdateDetailsDialog()


                // viewModel.sendLoginRequest( numberBinding!!.moNumber.text.toString().trim { it <= ' ' }, deviceToken, deviceId, deviceType)
                //  loading.showProgress()


            }

            override fun onError(responseException: QBResponseException?) {
                loading.hideProgress()
                //  longToast(R.string.update_user_error)
            }
        })
    }


    private fun loginToChat(qbUser: QBUser) {


        qbUser.password = mNumber
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
            // loading.hideProgress()

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        (numberBinding!!.moNumber as TextView).text = mNumber
        numberBinding!!.moNumber.requestFocus()
        numberBinding!!.moNumber.setSelection(numberBinding!!.moNumber.text.toString().length)

    }

    private fun storeqbid() {
        viewModel.storequickbloxid(
            this@MobileNumberActivity,
            this@MobileNumberActivity,
            SharedPrefsHelper.getQbUser()?.id.toString(),
            userid
        )
    }

}
