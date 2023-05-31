package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.databinding.ActivityEmailVerificationBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.EmailCodeResponse
import com.fitness.fitnessCru.response.ForgotPasswordResponse
import com.fitness.fitnessCru.response.OtpResponse
import com.fitness.fitnessCru.response.SignUpWithEmailResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.OnClick
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.EmailVerfiryViewModel
import com.fitness.fitnessCru.viewmodel.ForgotPasswordViewModel
import com.fitness.fitnessCru.viewmodel.OtpViewModel
import com.fitness.fitnessCru.viewmodel.SignUpWithEmailViewModel
import com.fitness.fitnessCru.vm_factory.EmailVerifyVMFactory
import com.fitness.fitnessCru.vm_factory.ForgotPasswordVMFactory
import com.fitness.fitnessCru.vm_factory.OtpVMFactory
import com.fitness.fitnessCru.vm_factory.SignUpWithEmailVMFactory
import java.text.DecimalFormat
import java.text.NumberFormat

class EmailVerificationActivity : AppCompatActivity() {

    private var binding: ActivityEmailVerificationBinding? = null

    private lateinit var loading: CustomProgressLoading

    private lateinit var repository: Repository
    private lateinit var viewModel: OtpViewModel
    private lateinit var viewModelFactory: OtpVMFactory

    private val deviceType = 0

    private lateinit var logType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        /* binding!!.emailidVerification.text = String.format(
             resources.getString(R.string._emailidverification),
             intent.extras!!.getString("userEmail") + " "
         )*/

        logType = intent.extras!!.getString("way").toString()

        repository = Repository()
        viewModelFactory = OtpVMFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            OtpViewModel::class.java
        )

        val styledText =
            "Enter OTP sent to <font color='#ffffff'><b>${intent.extras!!.getString("userEmail")}</b></font> to proceed"
        binding!!.emailidVerification.text = Html.fromHtml(styledText)

        loading = CustomProgressLoading(this)

        timerCountDown()

        //keyboard code start
        binding!!.otp.showSoftInputOnFocus = false
        binding!!.otp.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding!!.otp.setTextIsSelectable(false)
        val ic: InputConnection = binding!!.otp.onCreateInputConnection(EditorInfo())
        binding!!.keyboard.setInputConnection(ic)
        visibleKeyboard()
        //keyboard code end

        binding!!.resendOtp.setOnClickListener { resendOtp() }
        binding!!.proceedBtn.setOnClickListener { proceed() }

        goBack()

    }

    private fun resendOtp() {
        val email = intent.extras!!.getString("userEmail")

        if (logType == "forgot") {

            val repository by lazy { Repository() }
            val factory by lazy { ForgotPasswordVMFactory(repository) }
            val viewModel by lazy {
                ViewModelProvider(this@EmailVerificationActivity, factory).get(
                    ForgotPasswordViewModel::class.java
                )
            }

            loading.showProgress()
            viewModel.sendForgotPasswordRequest(email!!)

            viewModel.response.observe(this@EmailVerificationActivity) {
                loading.hideProgress()
                var userData = it.body()
                try {
                    if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {

                        Session.setToken(userData!!.data.access_token)

                        timerCountDown()

                    } else {
                        if (it.code() == 404) {
                            val error =
                                Util.error(it.errorBody(), ForgotPasswordResponse::class.java)
                            Toast.makeText(
                                applicationContext,
                                "${error.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            if (it.code() == 400) {
                                val error =
                                    Util.error(it.errorBody(), ForgotPasswordResponse::class.java)
                                Util.toast(this, "Error: ${error.message}")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Util.toast(this, "Error : ${e.message}")
                }
            }
        } else {

            val repository = Repository()
            val viewModelFactory = SignUpWithEmailVMFactory(repository)
            val viewModel = ViewModelProvider(this, viewModelFactory).get(
                SignUpWithEmailViewModel::class.java
            )
            viewModel.signUpWithEmail(email!!, deviceType)
            loading.showProgress()
            viewModel.response.observe(this) {
                loading.hideProgress()
                val signUpUser = it.body()
                try {
                    if (signUpUser != null && it.isSuccessful && signUpUser.error_code == 0) {
                        Session.setToken(signUpUser.data.access_token)

                        timerCountDown()

//                    Toast.makeText(
//                        this,
//                        signUpUser.data.email_verification_code,
//                        Toast.LENGTH_LONG
//                    ).show()

                    } else {
                        if (it.code() == 404) {
                            val error =
                                Util.error(it.errorBody(), SignUpWithEmailResponse::class.java)
                            Toast.makeText(
                                applicationContext,
                                "${error.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
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

    }

    private fun proceed() {
        val otp: String = binding!!.otp.text.toString().trim { it <= ' ' }
        val statusCode = intent.extras!!.getString("statusCode")
        val path = intent.extras?.getString("path").toString()
        loading.showProgress()

        if (otp.isEmpty()) {
            Toast.makeText(this@EmailVerificationActivity, "please enter Code", Toast.LENGTH_SHORT)
                .show()
            loading.hideProgress()
        } else {
            binding!!.keyboard.visibility = View.GONE

            if (logType == "forgot") {
                viewModel.otpFun(
                    Session.getToken().toString(),
                    otp,
                    null
                )
                //  SharedPrefsHelper.getQbUser().id.toString()

                viewModel.response.observe(this@EmailVerificationActivity) {
                    loading.hideProgress()
                    val response = it.body()
                    try {

                        if (response != null && it.isSuccessful && response.error_code ?: 1 == 0) {

                            binding!!.keyboard.visibility = View.GONE

                            val intent = Intent(
                                this@EmailVerificationActivity, CreatePasswordActivity::class.java
                            )
                            intent.putExtra("path", path)
                            startActivity(intent)

                        } else {
                            if (it.code() == 404) {
                                val error = Util.error(it.errorBody(), OtpResponse::class.java)
                                Toast.makeText(
                                    this@EmailVerificationActivity,
                                    "${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (it.code() == 400) {
                                try {
                                    val error =
                                        Util.error(it.errorBody(), OtpResponse::class.java)
                                    Toast.makeText(
                                        this@EmailVerificationActivity,
                                        error.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@EmailVerificationActivity,
                                        "invalid otp error",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }

                    } catch (e: Exception) {
                        /* Toast.makeText(
                             this@EmailVerificationActivity,
                             "Error : ${e.message}",
                             Toast.LENGTH_SHORT
                         ).show()*/
                    }
                }
            } else {
                val repository = Repository()
                val viewModelFactory = EmailVerifyVMFactory(repository)
                val viewModel = ViewModelProvider(this, viewModelFactory).get(
                    EmailVerfiryViewModel::class.java
                )

                viewModel.emailCodeFun(Session.getToken()!!, otp)
                viewModel.response.observe(this@EmailVerificationActivity) {
                    loading.hideProgress()
                    val response = it.body()
                    try {
                        if (response != null && it.isSuccessful && response.error_code == 0) {

                            binding!!.keyboard.visibility = View.GONE

                            if (path == "activation") {
                                val intent = Intent(
                                    this@EmailVerificationActivity, DashboardActivity::class.java
                                )
                                startActivity(intent)
                            } else {
                                val intent = Intent(
                                    this@EmailVerificationActivity,
                                    CreatePasswordActivity::class.java
                                )
                                intent.putExtra("path", path)
                                startActivity(intent)
                            }
                            finish()
                        } else {
                            if (it.code() == 400 || it.code() == 401) {
                                try {
                                    val error =
                                        Util.error(it.errorBody(), EmailCodeResponse::class.java)
                                    Toast.makeText(
                                        this@EmailVerificationActivity,
                                        error.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@EmailVerificationActivity,
                                        "invalid otp error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@EmailVerificationActivity,
                                    "Please enter correct OTP ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EmailVerificationActivity,
                            "Error : ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TAG", "proceed: ${e.toString()}")
                    }
                }
            }
        }
    }

    private fun timerCountDown() {
        binding!!.timer.visibility = View.VISIBLE
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding!!.resendOtp.isEnabled = false
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding!!.timer.text = f.format(min) + ":" + f.format(
                    sec
                )
            }

            override fun onFinish() {
                binding!!.timer.text = "Time's finished!"
                binding!!.timer.visibility = View.GONE

                binding!!.resendOtp.isEnabled = true
            }
        }.start()
    }

    private fun visibleKeyboard() {
        binding!!.apply {
            keyboard.call(object : OnClick {
                override fun click() {
                    keyboard.visibility = View.GONE
                }
            })

            otp.setOnTouchListener { p0, p1 ->
                if (!keyboard.isVisible)
                    keyboard.visibility = View.VISIBLE
                true
            }
        }
    }

    private fun goBack() {
        binding!!.gobackbtn.setOnClickListener {
            /* startActivity(
                 Intent(
                     this@EmailVerificationActivity,
                     SignUpWithEmailActivity*//*LoginMainActivity*//*::class.java
                )
            )*/
            this.onBackPressed()
            finish()
        }
    }
}
