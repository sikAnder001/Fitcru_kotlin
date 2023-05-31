package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.body.SignInBody
import com.fitness.fitnessCru.databinding.ActivityDummySignInBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.SignInResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEFAULT_USER_PASSWORD
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.utility.Utils.validEmail
import com.fitness.fitnessCru.viewmodel.SignInViewModel
import com.fitness.fitnessCru.vm_factory.SignInVMFactory
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

class DummySignInActivity : AppCompatActivity() {
    private var TAG = DummySignInActivity::class.java.simpleName
    private lateinit var dummySignInBinding: ActivityDummySignInBinding
    private var isRetypePasswordVisible = false
    private var isPasswordVisible = false
    private lateinit var loading: CustomProgressLoading
    private lateinit var user: QBUser

    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dummySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_dummy_sign_in)
        loading = CustomProgressLoading(this)
        view()
        registerSignUp()

    }

    private fun registerSignUp() {
        val signInRepository = Repository()
        val signInViewModelFactory = SignInVMFactory(signInRepository)
        signInViewModel = ViewModelProvider(
            this,
            signInViewModelFactory
        ).get(SignInViewModel::class.java)
        dummySignInBinding.btnRegister.setOnClickListener {


            val phoneNumber = dummySignInBinding.number.text.toString().trim()
            //  val quickblox_id = dummySignInBinding.quick_blox_id.text.toString().trim()
            if (validatePassword()) {
                if (phoneNumber.length != 10) {
                    dummySignInBinding.number.error =
                        resources.getString(R.string.mobile_number_must_have_10_digits)
                } else {

                    quickbloxlogin()
                }

            }

        }
        signInViewModel.response.observe(this@DummySignInActivity) {
            Log.e(TAG, " ${it.code()}")
            loading.hideProgress()
            try {
                if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {
                    val userSign = it.body()
                    Session.setToken(userSign!!.data.access_token)
                    Util.toast(
                        this@DummySignInActivity,
                        "  ${it.body()?.message}"
                    )
                    val gotoDashBoard = Intent(
                        this@DummySignInActivity,
                        QaCalendarActivity::class.java
                    )
                    startActivity(gotoDashBoard)
                } else {
                    if (it.code() == 400 || it.code() == 403 || it.code() == 401) {
                        val error = Util.error(
                            it.errorBody(),
                            SignInResponse::class.java
                        )
                        Toast.makeText(
                            this@DummySignInActivity,
                            "${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Util.toast(
                    this@DummySignInActivity,
                    "Something went wrong"
                )
                Log.e(TAG, e.toString())
            }

        }
        signInViewModel.response.observe(this@DummySignInActivity) {
            Log.e(TAG, " ${it.code()}")
            try {
                if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {
                    val userSign = it.body()
                    Session.setToken(userSign!!.data.access_token)
                    Util.toast(
                        this@DummySignInActivity,
                        "  ${it.body()?.message}"
                    )
                    val gotoDashBoard = Intent(
                        this@DummySignInActivity,
                        QaCalendarActivity::class.java
                    )
                    startActivity(gotoDashBoard)
                } else {
                    if (it.code() == 400 || it.code() == 403 || it.code() == 401) {
                        val error = Util.error(
                            it.errorBody(),
                            SignInResponse::class.java
                        )
                        Toast.makeText(
                            this@DummySignInActivity,
                            "${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Util.toast(
                    this@DummySignInActivity,
                    "Something went wrong"
                )
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun view() {
        dummySignInBinding.passVisibility.setOnClickListener(View.OnClickListener { togglePassVisability() })
        dummySignInBinding.confirmPassVisibility.setOnClickListener(View.OnClickListener { toggleReTypePassVisability() })
    }

    private fun validatePassword(): Boolean {
        val email = dummySignInBinding.email.text.toString().trim()
        val userName = dummySignInBinding.name.text.toString().trim()
        val phoneNumber = dummySignInBinding.number.text.toString().trim()
        val confirmPass = dummySignInBinding.confirmPass.text.toString().trim()
        val pass = dummySignInBinding.pass.text.toString().trim()

        if (userName.isEmpty()) {
            dummySignInBinding.name.error = resources.getString(R.string.please_enter_name)
            return false
        } else if (email.isEmpty()) {
            dummySignInBinding.email.error = resources.getString(R.string.please_enter_email)
            return false
        } else if (!validEmail(email)) {
            dummySignInBinding.email.error =
                resources.getString(R.string.please_enter_correct_email)
            return false
        } else if (phoneNumber.isEmpty()) {
            dummySignInBinding.number.error =
                resources.getString(R.string.please_enter_mobile_number)
            return false
        } else if (pass.isEmpty()) {
            dummySignInBinding.pass.error = resources.getString(R.string.please_enter_password)
            return false
        } else if (pass.length < 8) {
            dummySignInBinding.pass.error =
                resources.getString(R.string.password_must_be_8_digits)
            return false
        } else if (confirmPass.isEmpty()) {
            dummySignInBinding.confirmPass.error =
                resources.getString(R.string.please_enter_password)
            return false
        } else if (pass != confirmPass) {
            dummySignInBinding.confirmPass.error =
                resources.getString(R.string.password_doesnot_match)
            return false
        }
        return true
    }

    private fun togglePassVisability() {
        if (isPasswordVisible) {
            val pass = dummySignInBinding.pass.text.toString()
            dummySignInBinding.pass.transformationMethod =
                PasswordTransformationMethod.getInstance()
            dummySignInBinding.pass.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            dummySignInBinding.pass.setText(pass)
            dummySignInBinding.pass.setSelection(pass.length)
            dummySignInBinding.passVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_24))
        } else {
            val pass = dummySignInBinding.pass.text.toString()
            dummySignInBinding.pass.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            dummySignInBinding.pass.inputType = InputType.TYPE_CLASS_TEXT
            dummySignInBinding.pass.setText(pass)
            dummySignInBinding.pass.setSelection(pass.length)
            dummySignInBinding.passVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_off_24))
        }
        isPasswordVisible = !isPasswordVisible
    }

    private fun toggleReTypePassVisability() {
        if (isRetypePasswordVisible) {
            val pass = dummySignInBinding!!.confirmPass.text.toString()
            dummySignInBinding!!.confirmPass.transformationMethod =
                PasswordTransformationMethod.getInstance()
            dummySignInBinding!!.confirmPass.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            dummySignInBinding!!.confirmPass.setText(pass)
            dummySignInBinding!!.confirmPass.setSelection(pass.length)
            dummySignInBinding!!.confirmPassVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_24))
        } else {
            val pass = dummySignInBinding!!.confirmPass.text.toString()
            dummySignInBinding!!.confirmPass.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            dummySignInBinding!!.confirmPass.inputType = InputType.TYPE_CLASS_TEXT
            dummySignInBinding!!.confirmPass.setText(pass)
            dummySignInBinding!!.confirmPass.setSelection(pass.length)
            dummySignInBinding!!.confirmPassVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_visibility_off_24))
        }
        isRetypePasswordVisible = !isRetypePasswordVisible
    }

    private fun quickbloxlogin() {
        val user = createUserWithEnteredData()
        signUpNewUser(user)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()
        val userLogin = dummySignInBinding.email.text.toString().trim()
        val userFullName = dummySignInBinding.name.text.toString().trim()
        qbUser.login = userLogin
        qbUser.fullName = userFullName
        qbUser.password = DEFAULT_USER_PASSWORD
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
                if (e.httpStatusCode == com.fitness.fitnessCru.activities.ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
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
                /* OpponentsActivity.start(this@LoginActivity)
                 finish()*/
                /* if (cb_login.isChecked)
                     sharedPreference.isLogin = true
                 startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                 finishAffinity()*/
                //showLoginUpdateDetailsDialog()

                dummySignInBinding.apply {
                    this@DummySignInActivity.signInViewModel.sendSignInRequest(
                        SignInBody(
                            name.text.toString(),
                            email.text.toString(),
                            number.text.toString(),
                            pass.text.toString(),
                            confirmPass.text.toString(),

                            )
                    )
                    loading.showProgress()
                }

            }

            override fun onError(responseException: QBResponseException?) {
                loading.hideProgress()
                longToast(R.string.update_user_error)
            }
        })
    }


    private fun loginToChat(qbUser: QBUser) {
        qbUser.password = DEFAULT_USER_PASSWORD
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
