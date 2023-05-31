package com.fitness.fitnessCru.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityMainBinding
import com.fitness.fitnessCru.quickbox.executor.signInUser
import com.fitness.fitnessCru.quickbox.executor.signUp
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.EXTRA_LOGIN_RESULT_CODE
import com.fitness.fitnessCru.quickbox.utils.SharedPrefsHelper
import com.fitness.fitnessCru.repository.Repository
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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loading: CustomProgressLoading

    private lateinit var repository: Repository
    private lateinit var viewModel: EmailLoginViewModel
    private lateinit var factory: EmailLoginVMFactory

    private lateinit var user: QBUser
    var userid: String = ""
    private var twoHundredNull: Boolean = false

    private var isquickbloxapihit: Boolean = false

    private lateinit var deviceId: String
    private val deviceType = 0
    lateinit var deviceToken: String
    var quickId1 = ""

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private var RC_SIGN_IN = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

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

        val textShader: Shader = LinearGradient(
            510f, 400f, signUpMobileTV.textSize, signUpMobileTV.textSize, intArrayOf(
                Color.parseColor("#FF6105"),
                Color.parseColor("#FF02BD")
            ), null, Shader.TileMode.CLAMP
        )
        signUpMobileTV.paint.shader = textShader




        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        loading = CustomProgressLoading(this)
        binding.imageFacebook.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("signUp", "signUp")
            val intent = Intent(
                this,
                LoginMainActivity::class.java
            )
            intent.putExtras(bundle)
            startActivity(
                intent
            )
        }




        repository = Repository()
        factory = EmailLoginVMFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EmailLoginViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val account = GoogleSignIn.getLastSignedInAccount(this)

        binding.signInButton.setOnClickListener {

            loading.showProgress()
            signOut()
            signIn()
        }
        observerSignupSocial()
        observerSignupStore()
        view()
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
        }
    }

    private fun observerSignupSocial() {

        //isStoreapi.value = true
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

//                Hawk.put(QUICKBLOXID, body.data.social_id)

                //   Log.v("water", quickId)


                /* Hawk.put(SEMAIL, email)
                 Hawk.put(SOCIALID, socialId)
                 quickbloxlogin()*/


                /*Log.v("new23", quickbloxid)*/

                if (it.code() == 201) {
                    isquickbloxapihit = true

                    userid = body.data.id.toString()
                    /* if (body.data.quick_blox_id==null){
                         storeqbid()
                     }*/

                    /* viewModel.storequickbloxid(
                         this,
                         this,
                         SharedPrefsHelper.getQbUser().id.toString(),
                         body.data.id.toString()
                     )*/



                    Hawk.put(DEVICE_TOKEN, deviceToken)
                    /*     val intent = Intent(context, QaCalendarActivity::class.java)
                     intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                     context.startActivity(intent)*/

                } else {
                    isquickbloxapihit = false
                    userid = body.data.id.toString()
                    twoHundredNull = true

                    /*  if (body.data.quick_blox_id == null) {
                          userid = body.data.id.toString()
                          twoHundredNull = true
                      } else {
                          twoHundredNull = false
                          *//*  val intent = Intent(this, DashboardActivity::class.java)
                          intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                          this.startActivity(intent)*//*

                    }*/

                }
            } else if (it.isSuccessful && body != null && body!!.error_code == 1) {
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

    private fun observerSignupStore() {
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

                quickId1 = personId.toString()
//                quickId =personId.toString()

                /*val timer: CountDownTimer = object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {*/
                viewModel.sendSocialLoginRequest(
                    this@MainActivity,
                    applicationContext,
                    "1",
                    personId.toString(),
                    personEmail.toString(),
                    null,
                    deviceToken,
                    deviceId,
                    deviceType

                    /*         SharedPrefsHelper.getQbUser().id.toString()
    */
                )
                /*  }
              }.start()*/

                quickbloxlogin()

            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Apppname", "signInResult:failed code=" + e.statusCode)

        }
    }


    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                //  Toast.makeText(this, "User Log-Out", Toast.LENGTH_SHORT).show()
            }
    }

    private fun view() {
        binding!!.signUpMobileTV.setOnClickListener {


            startActivity(Intent(this@MainActivity, SighUpWithMobileActivity::class.java))
        }
        binding!!.loginEmailTV.setOnClickListener {

            startActivity(Intent(this@MainActivity, SignUpWithEmailActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, SplashActivity::class.java)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
        finish()
    }

    private fun quickbloxlogin() {
        val user = createUserWithEnteredData()
        signUpNewUser(user)
    }

    private fun createUserWithEnteredData(): QBUser {
        val qbUser = QBUser()

        qbUser.fullName = quickId1
        qbUser.login = quickId1
        qbUser.password = quickId1

        return qbUser
    }


    private fun signUpNewUser(newUser: QBUser) {
        //loading.hideProgress()
        // loading.showProgress()
        signUp(newUser, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                Log.v("movesucc", result.toString())


                SharedPrefsHelper.saveQbUser(newUser)
                loginToChat(result)
                //  longToast("quickNewUserCreated ID:"+SharedPrefsHelper.getQbUser().id.toString())


                if (isquickbloxapihit) {
                    storeqbid()
                    //   showToast("signup redirect")
                    startActivity(
                        Intent(this@MainActivity, QaCalendarActivity::class.java)

                    )
                } else {
                    if (twoHundredNull) {
                        storeqbid()
                    }
                    startActivity(
                        Intent(this@MainActivity, DashboardActivity::class.java)

                    )
                    Hawk.put(DEVICE_TOKEN, deviceToken)
                    OpponentsActivity.start(this@MainActivity)
                    finish()
                }


            }

            override fun onError(e: QBResponseException) {

                if (e.httpStatusCode == ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                    signInCreatedUser(newUser)
                } else {
                    loading.hideProgress()


                    //   showToast("error signup redirect")
                    startActivity(
                        Intent(this@MainActivity, QaCalendarActivity::class.java)

                    )
                    //add toast here


                    //   longToast(R.string.sign_up_error)
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
        // longToast("user udated Success block")
        user.password = null
        QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(updUser: QBUser?, params: Bundle?) {
                // longToast("UpdateUser ID:"+SharedPrefsHelper.getQbUser().id.toString())


                if (isquickbloxapihit) {
                    storeqbid()
                    //  showToast("update redirect")
                    startActivity(
                        Intent(this@MainActivity, QaCalendarActivity::class.java)

                    )
                } else {
                    if (twoHundredNull) {
                        storeqbid()
                    }
                    startActivity(
                        Intent(this@MainActivity, DashboardActivity::class.java)

                    )
                    Hawk.put(DEVICE_TOKEN, deviceToken)
                    OpponentsActivity.start(this@MainActivity)
                    finish()
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

        qbUser.password = quickId1
        user = qbUser


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
            this@MainActivity,
            this@MainActivity,
            SharedPrefsHelper.getQbUser()?.id.toString(),
            userid
        )
    }

}