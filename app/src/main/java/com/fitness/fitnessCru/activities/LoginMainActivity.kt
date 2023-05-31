package com.fitness.fitnessCru.activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityLoginMain2Binding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.EmailLoginResponse
import com.fitness.fitnessCru.response.SocialLoginResponse
import com.fitness.fitnessCru.response.StoreQuickbloxResponse
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.EmailLoginViewModel
import com.fitness.fitnessCru.vm_factory.EmailLoginVMFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import org.json.JSONException


//login
class LoginMainActivity : AppCompatActivity() {

    private var twoHundredNull: Boolean = false

    var userid: String = ""
    var useridEmail: String = ""

    private var isquickbloxapihit: Boolean = false

    private lateinit var user: QBUser

    private lateinit var binding: ActivityLoginMain2Binding
    private lateinit var loading: CustomProgressLoading
    private var userlog: EmailLoginResponse? = null
    private var isPasswordVisible = false
    private var callbackManager: CallbackManager? = null
    private var isSocial: Boolean = false
    private var isNotSocial: Boolean = false


    private lateinit var repository: Repository
    private lateinit var viewModel: EmailLoginViewModel
    private lateinit var factory: EmailLoginVMFactory

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private var RC_SIGN_IN = 101

    private lateinit var deviceId: String
    private val deviceType = 0
    lateinit var deviceToken: String
    var quickId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    deviceToken = token.toString()
                    Hawk.put(DEVICE_TOKEN, deviceToken)
                    Log.v("devicetokenLogin", deviceToken)


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

        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        repository = Repository()

        factory = EmailLoginVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[EmailLoginViewModel::class.java]

        loading = CustomProgressLoading(this)
        view()


        // storeqbid()


        if (intent.extras?.getString("signUp") != null) {
            binding.allItem.visibility = View.GONE
            binding.headerCons.visibility = View.GONE
            facebookLogin()
            isSocial = true
        } else {
            binding.allItem.visibility = View.VISIBLE
            binding.headerCons.visibility = View.VISIBLE
        }
        binding.forgetPassword.setOnClickListener {
            startActivity(
                Intent(this@LoginMainActivity, ForgetPasswordActivity::class.java)
            )
        }
        binding.imageFacebook.setOnClickListener {
            isSocial = true

            facebookLogin()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

//        val account = GoogleSignIn.getLastSignedInAccount(this)


        binding.signInButton.setOnClickListener {
            isSocial = true

            loading.showProgress()


            // quickbloxlogin()
//            signOut()
            signIn()
        }
        observerSocial()
        observerStore()
        // showToast("LoginMainActivity")
    }

    /*  override fun onResume() {
          super.onResume()
          isSocial=false
      }*/

    private fun observerSocial() {

        viewModel.responseGoogle.observe(this) {
            loading.showProgress()


            val body = it.body()!!

            if (it.isSuccessful && body != null && body.error_code == 0) {
                Util.toast(
                    this, body.message
                )

                Session.destroySession()
                Session.setToken(body.data.access_token)
                Hawk.put(ACCESS_TOKEN, it.body()!!.data.access_token)
                Hawk.put(DEVICE_TOKEN, deviceToken)
                Session.setToken(it.body()!!.data.access_token)
                Session.setUserDetails(body.data)
                var isloginsocialapi = true
                Hawk.put(ISLOGIN, isloginsocialapi)

                viewModel.saveFcmToken(this, applicationContext)
                //  showToast("social login")

                Log.v("water", quickId)

                if (it.code() == 201) {
                    isquickbloxapihit = true

                    userid = body.data.id.toString()
                    //   Hawk.put(DEVICE_TOKEN, deviceToken)


                } else {
                    isquickbloxapihit = false

                    userid = body.data.id.toString()
                    twoHundredNull = true
                    /* val newId =SharedPrefsHelper.getQbUser()?.id.toString()
                     body.data.quick_blox_id=newId*/
                    //  twoHundredNull = false


                }
            } else if (it.isSuccessful && body!!.error_code == 1) {
                loading.hideProgress()
                Util.toast(
                    this, it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.toast(
                    this,
                    Util.error(it.errorBody(), SocialLoginResponse::class.java).message
                )
                loading.hideProgress()
            }
        }

    }

    private fun observerStore() {
        viewModel.responseStoreId.observe(this) {
            val body = it.body()!!

            if (it.isSuccessful && body != null && body.error_code == 0) {

                // showToast("storedQuickbloxAPI")

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


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loading.hideProgress()
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)

        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

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

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl

                quickId = personId.toString()

                Log.v("googleone", quickId)

                viewModel.sendSocialLoginRequest(
                    this@LoginMainActivity,
                    applicationContext,
                    "1",
                    personId.toString(),
                    personEmail.toString(),
                    null,
                    deviceToken,
                    deviceId,
                    deviceType


                )


                quickbloxlogin()

            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Apppname", "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun view() {


        binding!!.gobackbtn.setOnClickListener {
            startActivity(
                Intent(this@LoginMainActivity, SplashActivity::class.java)
            )
        }
        binding!!.passVisibility.setOnClickListener { togglePassVisibility() }
        binding!!.loginmobileTV.setOnClickListener { moveToLogin() }
        binding!!.loginemailTV.setOnClickListener {
            isSocial = false
            isNotSocial = true

            if (isNotSocial) {
                loading.showProgress()
                quickbloxlogin()
            }


        }
    }

    private fun proceed() {
        val email: String = binding.emailET.text.trim().toString()
        val password: String = binding.password.text.trim().toString()

        if (isEmailValid(email)) {
            if (!password.isEmpty()) {
                if (deviceToken.isNotEmpty()) {
                    loading.showProgress()

                    /*   viewModel.sendEmailLoginRequest(
                            email,
                            password,
                            deviceToken,
                            deviceId,
                           deviceType,

                        )*/
                    viewModel.response.observe(this) {
                        loading.showProgress()
                        userlog = it.body()
                        try {
                            if (userlog != null && it.isSuccessful && (it.body()?.error_code ?: 1) == 0) {
                                if (it.code() == 201)
                                    Toast.makeText(
                                        applicationContext,
                                        userlog!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else {
                                    Session.destroySession()
                                    /*  Session.setToken(it.body()!!.data.access_token)
                                      Session.setUserDetails(userlog.data)*/
                                    /* startActivity(
                                         Intent(
                                             this@LoginMainActivity,
                                             DashboardActivity::class.java
                                         )
 */

                                    Hawk.put(ACCESS_TOKEN, it.body()!!.data.access_token)
                                    Hawk.put(DEVICE_TOKEN, deviceToken)
                                    Session.setToken(it.body()!!.data.access_token)
                                    Session.setUserDetails(userlog!!.data)
                                    /* startActivity(
                                         Intent(this@LoginMainActivity, DashboardActivity::class.java)

                                     )
                                     OpponentsActivity.start(this@LoginMainActivity)

                                     finish()*/
//                                    Log.v("FCMTT", Hawk.get<String?>(FCM_TOKEN).toString())
                                    viewModel.saveFcmToken(this, applicationContext)

                                    useridEmail = userlog!!.data.id.toString()

                                    //storeIdEmaillogin()

                                    if (userlog!!.data.quick_blox_id == null) {
                                        storeIdEmaillogin()

                                    }
                                    startActivity(
                                        Intent(
                                            this@LoginMainActivity,
                                            DashboardActivity::class.java
                                        )

                                    )
                                    Hawk.put(DEVICE_TOKEN, deviceToken)
                                    OpponentsActivity.start(this@LoginMainActivity)
                                    finish()


                                    /*  )
                                                       OpponentsActivity.start(this@LoginMainActivity)
                                                       finish()
                                                   }*/

                                }


                            } else {
                                if (it.code() == 471) {
                                    val dialogBuilder =
                                        AlertDialog.Builder(
                                            this@LoginMainActivity,
                                            R.style.AlertDialogStyle
                                        )

                                    dialogBuilder.setMessage("Do you want to activate your account?")
                                        .setCancelable(false)
                                        .setPositiveButton(
                                            "Yes",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                viewModel.activateEmailAccount(email)
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
                                    try {

                                        val error =
                                            Util.error(
                                                it.errorBody(),
                                                EmailLoginResponse::class.java
                                            )
                                        Toast.makeText(
                                            this@LoginMainActivity,
                                            error.message /*${it.body()!!.error_code}"*/,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } catch (e: Exception) {
                                        0
                                    }
                                }
                            }
                        } catch (e: Exception) {


                            Toast.makeText(
                                this@LoginMainActivity,
                                "Error : ${e.message},",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                    viewModel.sendEmailLoginRequest(
                        binding.emailET.text.trim().toString(),
                        binding.password.text.trim().toString(),
                        deviceToken,
                        deviceId,
                        deviceType

                    )
                } else {

                    Toast.makeText(this, "Please enter valid credential", Toast.LENGTH_SHORT).show()
                    loading.hideProgress()
                }

            } else {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                loading.hideProgress()
            }

        } else {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
            loading.hideProgress()
        }

        //activation response
        viewModel.emailActivation.observe(this) {
            loading.hideProgress()
            val userData = it.body()
            if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {
                Session.destroySession()

                Session.setToken(userData!!.data.access_token)
                Session.setUserDetails(userData.data)
                viewModel.saveFcmToken(this, applicationContext)

                val intent = Intent(this, EmailVerificationActivity::class.java)
                var bundle = Bundle()
                bundle.putString(
                    "email_verification_code",
                    userData!!.data.email_verification_code
                )
                bundle.putString("userEmail", userData.data.email)
                bundle.putString("statusCode", it.code().toString())
                bundle.putString("path", "activation")
                intent.putExtras(bundle)
                startActivity(intent)
//                Util.toast(this, userData.data.email_verification_code.toString())
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

    private fun moveToLogin() {
        startActivity(Intent(applicationContext, MobileNumberActivity::class.java))
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

    private fun facebookLogin() {
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email", "public_profile"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onCancel() {
                    if (intent.extras?.getString("signUp") != null) {
                        startActivity(Intent(this@LoginMainActivity, MainActivity::class.java))
                    }
                    Util.toast(applicationContext, "You've cancelled")

                }

                override fun onError(exception: FacebookException) {
                    if (intent.extras?.getString("signUp") != null) {
                        startActivity(Intent(this@LoginMainActivity, MainActivity::class.java))
                    }
                    Util.toast(applicationContext, exception.message.toString())
                }

                override fun onSuccess(result: LoginResult) {
                    val request = GraphRequest.newMeRequest(result?.accessToken) { `object`, _ ->
                        Log.d("TAG", `object`.toString())
                        try {
                            val email = `object`?.getString("email")
                            val id = `object`?.getString("id")

                            // Toast.makeText(applicationContext, "success worked", Toast.LENGTH_SHORT).show()
                            quickId = id.toString()

                            Log.v("fbone", quickId)

/*                            val timer: CountDownTimer = object : CountDownTimer(5000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {}
                                override fun onFinish() {*/
                            viewModel.sendSocialLoginRequest(
                                this@LoginMainActivity,
                                applicationContext,
                                "2",
                                id.toString(),
                                email.toString(),
                                deviceToken = deviceToken,
                                deviceId = deviceId,
                                deviceType = deviceType


                            )

                            /*     }
                             }.start()*/

                            quickbloxlogin()


                            /* if (intent.extras?.getString("signUp") != null) {
                                 startActivity(
                                     Intent(
                                         this@LoginMainActivity,
                                         MainActivity::class.java


                                     )


                                 )

                                 startActivity(
                                     Intent(this@LoginMainActivity, DashboardActivity::class.java)

                                 )
                                 OpponentsActivity.start(this@LoginMainActivity)

                                 finish()

                             }*/
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "first_name,last_name,email,id")
                    request.parameters = parameters
                    request.executeAsync()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SplashActivity::class.java))
        super.onBackPressed()
    }


    private fun quickbloxlogin() {
        // showToast("quickblox")


        val user = createUserWithEnteredData()
        signUpNewUser(user)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()

        val userEmail = binding.emailET.text.trim().toString()
        val userPassword = binding.password.text.trim().toString()


        if (isSocial) {
            qbUser.fullName = quickId
            qbUser.login = quickId
            qbUser.password = quickId


        } else {
            qbUser.fullName = userEmail
            qbUser.login = userEmail
            qbUser.password = userEmail
            qbUser.password = userEmail
        }
        return qbUser
    }


    private fun signUpNewUser(newUser: QBUser) {
        //loading.hideProgress()
        // loading.showProgress()
        signUp(newUser, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                //  longToast("quickblox signup")
                //   Log.v("movesucc", result.toString())


                SharedPrefsHelper.saveQbUser(newUser)
                loginToChat(result)
                //  longToast("quickNewUserCreated ID:"+SharedPrefsHelper.getQbUser().id.toString())

                if (isSocial) {
                    if (isquickbloxapihit) {
                        storeqbid()
                        //   showToast("signup redirect")
                        val intent = Intent(this@LoginMainActivity, QaCalendarActivity::class.java)
                        startActivity(intent)
                    } else {
                        if (twoHundredNull) {
                            storeqbid()
                        }
                        startActivity(
                            Intent(this@LoginMainActivity, DashboardActivity::class.java)

                        )
                        Hawk.put(DEVICE_TOKEN, deviceToken)
                        OpponentsActivity.start(this@LoginMainActivity)
                        finish()
                    }
                }

                if (isNotSocial) {
                    loading.showProgress()
                    // showToast("click proceed")
                    proceed()

                }


            }

            override fun onError(e: QBResponseException) {

                if (e.httpStatusCode == ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                    signInCreatedUser(newUser)
                } else {
                    loading.hideProgress()

                    if (isSocial) {
                        //   showToast("error signup redirect")
                        val intent = Intent(this@LoginMainActivity, QaCalendarActivity::class.java)
                        startActivity(intent)
                        //add toast here
                    }

                    // longToast(R.string.sign_up_error)
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
                Log.v("movecreateerrr", "worked")


                loading.hideProgress()
                //  longToast(R.string.sign_in_error)
            }
        })
    }


    private fun updateUserOnServer(user: QBUser) {
        //   longToast("user udated Success block")
        user.password = null
        QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(updUser: QBUser?, params: Bundle?) {
                // longToast("UpdateUser ID:"+SharedPrefsHelper.getQbUser().id.toString())

                if (isSocial) {
                    if (isquickbloxapihit) {
                        storeqbid()
                        // showToast("update redirect")
                        startActivity(
                            Intent(this@LoginMainActivity, QaCalendarActivity::class.java)

                        )
                    } else {
                        if (twoHundredNull) {
                            storeqbid()
                        }
                        startActivity(
                            Intent(this@LoginMainActivity, DashboardActivity::class.java)

                        )
                        Hawk.put(DEVICE_TOKEN, deviceToken)
                        OpponentsActivity.start(this@LoginMainActivity)
                        finish()
                    }
                }

                if (isNotSocial) {
                    loading.showProgress()
                    proceed()


                }
            }

            override fun onError(responseException: QBResponseException?) {
                loading.hideProgress()
                //  longToast(R.string.update_user_error)
            }
        })
    }


    private fun loginToChat(qbUser: QBUser) {
        val userEmail = Session.getEmailForCreate()
        if (isSocial) {
            qbUser.password = quickId
            user = qbUser

        } else {
            qbUser.password = userEmail
            user = qbUser

        }
        startLoginService(qbUser)

    }

    private fun startLoginService(qbUser: QBUser) {

        //  storeqbid2()

        val tempIntent = Intent(this, LoginService::class.java)
        val pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
        LoginService.start(this, qbUser, pendingIntent)


    }

    private fun storeqbid() {
        viewModel.storequickbloxid(
            this@LoginMainActivity,
            this@LoginMainActivity,
            SharedPrefsHelper.getQbUser()?.id.toString(),
            userid
        )
    }

    private fun storeIdEmaillogin() {
        viewModel.storequickbloxid(
            this@LoginMainActivity,
            this@LoginMainActivity,
            SharedPrefsHelper.getQbUser()?.id.toString(),
            useridEmail
        )
    }


}