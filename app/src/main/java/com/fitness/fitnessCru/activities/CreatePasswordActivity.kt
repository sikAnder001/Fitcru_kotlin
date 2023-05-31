package com.fitness.fitnessCru.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.body.ChangePasswordBody
import com.fitness.fitnessCru.databinding.ActivityCreatePasswordBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CreatePasswordResponse
import com.fitness.fitnessCru.utility.ACCESS_TOKEN
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CreatePasswordViewModel
import com.fitness.fitnessCru.vm_factory.CreatePasswordVMFactory
import com.orhanobut.hawk.Hawk
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

class CreatePasswordActivity : AppCompatActivity() {
    private lateinit var user: QBUser
    var binding: ActivityCreatePasswordBinding? = null
    private var isPasswordVisible = false
    private var isRetypePasswordVisible = false
    private lateinit var loading: CustomProgressLoading

    private lateinit var viewModel: CreatePasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        loading = CustomProgressLoading(this)


        view()
        callApi()
    }

    private fun callApi() {
        try {
            val repository by lazy { Repository() }
            val factory by lazy { CreatePasswordVMFactory(repository) }
            viewModel =
                ViewModelProvider(
                    this,
                    factory
                ).get(CreatePasswordViewModel::class.java)

            binding!!.proceedBtn.setOnClickListener {
                if (validatePassword()) {
                    loading.showProgress()
                    //  quickbloxlogin()

                    viewModel.createPassword(
                        ChangePasswordBody(
                            "",
                            binding!!.password.text.toString().trim(),
                            binding!!.reEnterpassword.text.toString().trim(),
                            SharedPrefsHelper.getQbUser().id.toString()

                        )
                    )

                }
            }
            viewModel.response.observe(this) {
                if (it.isSuccessful && it.body()!!.error_code == 0) {
                    /*Util.toast(this, it.body()!!.message)*/
                    var path = intent.getStringExtra("path").toString() ?: ""


                    when (path) {
                        "forget" -> {
                            startActivity(Intent(this, LoginMainActivity::class.java))
                            finishAffinity()
                        }
                        "email" -> {
                            Hawk.put(ACCESS_TOKEN, Session.getToken())

                            startActivity(
                                Intent(
                                    this@CreatePasswordActivity,
                                    QaCalendarActivity::class.java
                                )
                            )
                            finishAffinity()
                        }
                        else -> {
                        }
                    }
                } else if (it.isSuccessful && it.body()!!.error_code == 1) {
                    Util.toast(this, it.body()!!.message)
                } else if (!it.isSuccessful) {
                    val error = Util.error(it.errorBody(), CreatePasswordResponse::class.java)
                    Util.toast(this, error.message)
                }
                loading.hideProgress()
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "getDietType: $e")
        }
    }

    private fun view() {
        binding!!.gobackbtn.setOnClickListener { onBackPressed() }
        binding!!.passVisibility.setOnClickListener { togglePassVisibility() }
        binding!!.reEnterpassVisibility.setOnClickListener { toggleReTypePassVisibility() }
    }

    private fun validatePassword(): Boolean {
        val pass = binding!!.password.text.toString().trim { it <= ' ' }
        val reTypePass = binding!!.reEnterpassword.text.toString().trim { it <= ' ' }
        if (pass.length < 8) {
            binding!!.password.error = resources.getString(R.string.min_length)
            return false
        }
        if (reTypePass.length < 8) {
            binding!!.reEnterpassword.error =
                resources.getString(R.string.min_length)
            return false
        }
        if (pass != reTypePass) {
            binding!!.reEnterpassword.error = resources.getString(R.string.password_not_matched)
            return false
        }
        return true
    }

    override fun onBackPressed() {
        this.finish()
    }

    private fun togglePassVisibility() {
        if (isPasswordVisible) {
            val pass = binding!!.password.text.toString()
            binding!!.password.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.password.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding!!.password.setText(pass)
            binding!!.password.setSelection(pass.length)
            binding!!.passVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_24))
        } else {
            val pass = binding!!.password.text.toString()
            binding!!.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding!!.password.inputType = InputType.TYPE_CLASS_TEXT
            binding!!.password.setText(pass)
            binding!!.password.setSelection(pass.length)
            binding!!.passVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_off_24))
        }
        isPasswordVisible = !isPasswordVisible
    }

    private fun toggleReTypePassVisibility() {
        if (isRetypePasswordVisible) {
            val pass = binding!!.reEnterpassword.text.toString()
            binding!!.reEnterpassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding!!.reEnterpassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding!!.reEnterpassword.setText(pass)
            binding!!.reEnterpassword.setSelection(pass.length)
            binding!!.reEnterpassVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_24))
        } else {
            val pass = binding!!.reEnterpassword.text.toString()
            binding!!.reEnterpassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding!!.reEnterpassword.inputType = InputType.TYPE_CLASS_TEXT
            binding!!.reEnterpassword.setText(pass)
            binding!!.reEnterpassword.setSelection(pass.length)
            binding!!.reEnterpassVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_off_24))
        }
        isRetypePasswordVisible = !isRetypePasswordVisible
    }


    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        val userEmail = Session.getEmailForCreate()
        if (userEmail != null) {
            Log.v("email", userEmail)
        }
        val userEpass = userEmail
        val userREpass = userEmail

        /*  val userEpass = binding!!.password.text.toString().trim { it <= ' ' }
          val userREpass = binding!!.reEnterpassword.text.toString().trim { it <= ' ' }*/
        qbUser.password = userEpass
        qbUser.password = userREpass
        //qbUser.fullName = userFullName
        qbUser.password = userEmail
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

                viewModel.createPassword(
                    ChangePasswordBody(
                        "",
                        binding!!.password.text.toString().trim(),
                        binding!!.reEnterpassword.text.toString().trim(),
                        ""

                    )
                )

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
        val userEmail = Session.getEmailForCreate()
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
