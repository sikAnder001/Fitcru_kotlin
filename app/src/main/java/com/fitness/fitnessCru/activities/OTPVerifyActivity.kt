package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityOtpverifyBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.OtpResponse
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.OtpViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.OtpVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.orhanobut.hawk.Hawk
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import java.text.DecimalFormat
import java.text.NumberFormat

class OTPVerifyActivity : AppCompatActivity() {

    private var otpverifyBinding: ActivityOtpverifyBinding? = null
    private lateinit var user: QBUser

    private lateinit var loading: CustomProgressLoading
    private lateinit var intentotp: String
    private lateinit var logType: String
    private lateinit var repository: Repository
    private lateinit var viewModel: OtpViewModel
    private lateinit var viewModelFactory: OtpVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpverifyBinding = ActivityOtpverifyBinding.inflate(layoutInflater)
        setContentView(otpverifyBinding!!.root)

        getUserDetail()

        logType = intent.extras!!.getString("type").toString()

        timerCountDown()
        otpverifyBinding!!.number.text = intent.extras!!.getString("number").toString()

        loading = CustomProgressLoading(this)

        intentotp = intent.extras!!.getString("otp").toString()
        //keyboard code start
        otpverifyBinding!!.otp.showSoftInputOnFocus = false
        otpverifyBinding!!.otp.setRawInputType(InputType.TYPE_CLASS_TEXT)
        otpverifyBinding!!.otp.setTextIsSelectable(false)
        val ic: InputConnection = otpverifyBinding!!.otp.onCreateInputConnection(EditorInfo())
        otpverifyBinding!!.keyboard.setInputConnection(ic)
        visibleKeyboard()
        //keyboard code end
        otpverifyBinding!!.resendOtp.setOnClickListener { resendOtp() }

        resposeObj()
        otpverifyBinding!!.proceedBtn.setOnClickListener {
            proceed()
        }

        goBack()
    }

    private fun resposeObj() {
        repository = Repository()
        viewModelFactory = OtpVMFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            OtpViewModel::class.java
        )
    }

    private fun resendOtp() {
        val moNumber = intent.extras!!.getString("number")

        otpverifyBinding!!.keyboard.visibility = View.GONE
        loading.showProgress()
        viewModel.resendotpFun(Session.getToken().toString(), moNumber!!)
        //SharedPrefsHelper.getQbUser().id.toString()
        viewModel.response1.observe(this@OTPVerifyActivity) {
            loading.hideProgress()

            val response = it.body()
            try {
                if (response != null && it.isSuccessful && response.error_code ?: 1 == 0) {
                    intentotp = response.data.otp
                    timerCountDown()
                    otpverifyBinding!!.keyboard.visibility = View.GONE
                } else {
                    if (it.code() == 401 || it.code() == 400) {
                        val error = Util.error(it.errorBody(), OtpResponse::class.java)
                        Toast.makeText(
                            this@OTPVerifyActivity,
                            error.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@OTPVerifyActivity,
                    "Error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun proceed() {

        val otp: String = otpverifyBinding!!.otp.text.toString().trim { it <= ' ' }




        if (otp.isEmpty()) {
            Toast.makeText(this@OTPVerifyActivity, "please enter OTP", Toast.LENGTH_SHORT)
                .show()
        } else {
            loading.showProgress()
            otpverifyBinding!!.keyboard.visibility = View.GONE
            // quickbloxlogin()
            viewModel.otpFun(
                Session.getToken().toString(),
                otp,
                SharedPrefsHelper.getQbUser().id.toString()
            )
            //  SharedPrefsHelper.getQbUser().id.toString()

            viewModel.response.observe(this@OTPVerifyActivity) {
                loading.hideProgress()
                val response = it.body()
                try {


                    if (response != null && it.isSuccessful && response.error_code ?: 1 == 0) {
                        Hawk.put(ACCESS_TOKEN, Session.getToken())

                        otpverifyBinding!!.keyboard.visibility = View.GONE

                        if (logType.equals("logIn")) {
                            startActivity(
                                Intent(
                                    this@OTPVerifyActivity,
                                    DashboardActivity::class.java
                                )

                            )
                            OpponentsActivity.start(this@OTPVerifyActivity)
                            finishAffinity()
                        } else if (logType == "signUp") {
                            startActivity(
                                Intent(
                                    this@OTPVerifyActivity,
                                    QaCalendarActivity::class.java
                                )
                            )
                        }

                    } else {
                        if (it.code() == 404) {
                            val error = Util.error(it.errorBody(), OtpResponse::class.java)
                            Toast.makeText(
                                this@OTPVerifyActivity,
                                "${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        if (it.code() == 400) {
                            try {
                                val error =
                                    Util.error(it.errorBody(), OtpResponse::class.java)
                                Toast.makeText(
                                    this@OTPVerifyActivity,
                                    error.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@OTPVerifyActivity,
                                    "invalid otp error",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@OTPVerifyActivity,
                        "Error : ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getUserDetail() {
        val repository by lazy { Repository() }
        val factory by lazy { UserDetailsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[UserDetailsViewModel::class.java]
        }
        viewModel.getUserDetails()
        viewModel.response.observe(this@OTPVerifyActivity) {

            if (it.isSuccessful && it.code() == 200) {
                val data = it.body()!!.data
                if (data.name != null || data.email != null) {
                    Session.setUserDetails(data)
                }
            } else {
            }
        }
    }

    private fun timerCountDown() {
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                otpverifyBinding!!.resendOtp.isEnabled = false
                otpverifyBinding!!.timer.visibility = View.VISIBLE
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                otpverifyBinding!!.timer.setText(
                    f.format(min) + ":" + f.format(
                        sec
                    )
                )
            }

            override fun onFinish() {
                otpverifyBinding!!.timer.setText("Time's finished!")
                otpverifyBinding!!.timer.visibility = View.GONE

                otpverifyBinding!!.resendOtp.isEnabled = true
            }
        }.start()
    }

    private fun visibleKeyboard() {
        otpverifyBinding!!.apply {
            keyboard.call(object : OnClick {
                override fun click() {
                    keyboard.visibility = View.GONE
                }
            })

            otp.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if (!keyboard.isVisible)
                        keyboard.visibility = View.VISIBLE
                    return true
                }
            })
        }
    }

    private fun goBack() {
        otpverifyBinding!!.gobackbtn.setOnClickListener {
            if (logType == "logIn") {
                onBackPressed()
                finish()
            } else if (logType == "signUp") {
                onBackPressed()
                finish()
            }
        }
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        val userMobile = Session.getMobileForCreate()
        /*val userOtp = otpverifyBinding!!.otp.text.toString().trim { it <= ' ' }*/
        val userOtp = userMobile

        // val userREpass = binding!!.reEnterpassword.text.toString().trim { it <= ' ' }
        qbUser.password = userOtp

        // qbUser.password = userREpass
        //qbUser.fullName = userFullName
        qbUser.password = userMobile
        return qbUser
    }

    private fun signUpNewUser(newUser: QBUser) {
        loading.hideProgress()
        loading.showProgress()
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
                loading.hideProgress()
                /*OpponentsActivity.start(this@LoginActivity)
               finish()
               if (cb_login.isChecked)
                   sharedPreference.isLogin = true
               startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
               finishAffinity()*/
                //showLoginUpdateDetailsDialog()


//                viewModel.createPassword(
//                    ChangePasswordBody(
//                        "",
//                        binding!!.password.text.toString().trim(),
//                        binding!!.reEnterpassword.text.toString().trim(),
//                        ""
//
//                    )
//                )


                // viewModel.sendLoginRequest(numberBinding!!.moNumber.text.toString().trim(), deviceToken, deviceId, deviceType,"")
                //  viewModel.sendLoginRequest( numberBinding!!.moNumber.text.toString().trim { it <= ' ' }, deviceToken, deviceId, deviceType)
                loading.showProgress()


            }

            override fun onError(responseException: QBResponseException?) {
                loading.hideProgress()
                longToast(R.string.update_user_error)
            }
        })
    }


    private fun loginToChat(qbUser: QBUser) {

        val userMobile = Session.getMobileForCreate()
        qbUser.password = userMobile
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
            loading.hideProgress()

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
