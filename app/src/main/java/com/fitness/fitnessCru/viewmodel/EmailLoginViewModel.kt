package com.fitness.fitnessCru.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import com.fitness.fitnessCru.utility.Util
import kotlinx.coroutines.launch
import retrofit2.Response

class EmailLoginViewModel(private val repository: Repository) : ViewModel() {
    //viewmodel
    val response = MutableLiveData<Response<EmailLoginResponse>>()
    val responseGoogle = MutableLiveData<Response<SocialLoginResponse>>()

    // val responseEGoogle = MutableLiveData<Response<NewSocialResponse>>()
    val responseStoreId = MutableLiveData<Response<StoreQuickbloxResponse>>()
    val emailActivation = MutableLiveData<Response<SignUpWithEmailResponse>>()
    val fcmTokenResponse = MutableLiveData<Response<FcmTokenResponse>>()
    var userid: String = ""


    fun sendEmailLoginRequest(
        email: String,
        password: String,
        deviceToken: String,
        deviceId: String,
        deviceType: Int
    ) {
        viewModelScope.launch {
            response.value =
                repository.emailLogin(email, password, deviceToken, deviceId, deviceType)
        }
    }

    fun storequickbloxid(
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        quickBloxId: String,
        user_id: String
    ) {
        viewModelScope.launch {
            responseStoreId.value =
                repository.storequickbloxid(quickBloxId, user_id)

        }
    }


    fun sendSocialLoginRequest(
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        providerId: String,
        socialId: String,
        email: String,
        phone: String? = null,
        deviceToken: String,
        deviceId: String,
        deviceType: Int,


        ) {
        viewModelScope.launch {
            /*  responseGoogle.observe(viewLifecycleOwner) {
                  val body = it.body()!!

                  if (it.isSuccessful && body != null && body.error_code == 0) {
                      Util.toast(
                          context, body.message
                      )

                      Session.destroySession()
                      Session.setToken(body.data.access_token)
                      Hawk.put(ACCESS_TOKEN, it.body()!!.data.access_token)
                      Hawk.put(DEVICE_TOKEN, deviceToken)
                      Session.setToken(it.body()!!.data.access_token)
                      Session.setUserDetails(body.data)
                      var isloginsocialapi = true
                      Hawk.put(ISLOGIN, isloginsocialapi)


                      saveFcmToken(viewLifecycleOwner, context)




                      Hawk.put(SEMAIL, email)
                      Hawk.put(SOCIALID, socialId)
                      quickbloxlogin()

                      Log.v("logintag",SharedPrefsHelper.getQbUser().id.toString())




                      *//*Log.v("new23", quickbloxid)*//*

                    if (it.code() == 201) {

                        isquickbloxapihit = true
                        userid = body.data.id.toString()


                        Log.v("storetag",getQbUser().id.toString())




                        *//* storequickbloxid(
                             viewLifecycleOwner,
                             context,
                             SharedPrefsHelper.getQbUser().id.toString(),
                             body.data.id.toString()
                         )*//*





                        Hawk.put(DEVICE_TOKEN, deviceToken)
                   *//*     val intent = Intent(context, QaCalendarActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)*//*

                    } else {
                        isquickbloxapihit = false



                        *//*storequickbloxid(
                            viewLifecycleOwner,
                            context,
                            quickBloxId =SharedPrefsHelper.getQbUser().id.toString(),
                            body.data.id.toString()
                        )*//*


                        val intent = Intent(context, DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)

                        Hawk.put(DEVICE_TOKEN, deviceToken)
                        val intent2 = Intent(context, OpponentsActivity::class.java)
                        intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent2)


                    }
                } else if (it.isSuccessful && body != null && body!!.error_code == 1) {
                    Util.toast(
                        context, it.body()!!.message
                    )
                } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                    context,
                    Util.error(it.errorBody(), ChooseMonthlyPlanResponse::class.java).message
                )
            }*/
            responseGoogle.value =
                repository.sendSocialLoginRequest(
                    providerId,
                    socialId,
                    email,
                    phone,
                    deviceToken,
                    deviceId,
                    deviceType

                )

        }
    }
/*

     fun sendSocialLogin(
         viewLifecycleOwner: LifecycleOwner,
         context: Context,
         providerId: String,
         socialId: String,
         email: String,
         phone: String? = null,
         deviceToken: String,
         deviceId: String,
         deviceType: Int

     ) {
         viewModelScope.launch {
             responseGoogle.value =
                 repository.sendSocialLoginRequest(
                     providerId,
                     socialId,
                     email,
                     phone,
                     deviceToken,
                     deviceId,
                     deviceType,

                 )
         }

     }*/

    /*  private fun quickbloxlogin() {
          val user = createUserWithEnteredData()
          signUpNewUser(user)
      }

      private fun createUserWithEnteredData(): QBUser {
          val qbUser = QBUser()
          // val userLogin =   binding.emailET.text.trim().toString()
          // val userEmail = binding.emailET.text.trim().toString()
          // val userPassword = binding.password.text.trim().toString()
          //  qbUser.fullName = userlog?.data?.name

          qbUser.fullName = Hawk.get(SOCIALID)
          qbUser.login = Hawk.get(SOCIALID)
          qbUser.password = Hawk.get(SOCIALID)
          // qbUser.password = userEmail
          return qbUser
      }
      *//* internal fun showProgressDialog(@StringRes messageId: Int) {
         if (progressDialog == null) {
             progressDialog = ProgressDialog(this)
             progressDialog?.isIndeterminate = true
             progressDialog?.setCancelable(false)
             progressDialog?.setCanceledOnTouchOutside(false)
             progressDialog?.setOnKeyListener(KeyEventListener())
         }
         progressDialog?.setMessage(getString(messageId))
         progressDialog?.show()
     }

     internal fun hideProgressDialog() {
         if (progressDialog?.isShowing == true) {
             progressDialog?.dismiss()
         }
     }

     inner class KeyEventListener : DialogInterface.OnKeyListener {
         override fun onKey(dialog: DialogInterface?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
             return keyCode == KeyEvent.KEYCODE_BACK
         }
     }*//*

    private fun signUpNewUser(newUser: QBUser) {
        //loading.hideProgress()
        // loading.showProgress()
        signUp(newUser, object : QBEntityCallback<QBUser> {
            override fun onSuccess(result: QBUser, params: Bundle) {
                SharedPrefsHelper.saveQbUser(newUser)


                loginToChat(result)


                *//*  SharedPrefsHelper.saveQbUser(newUser)
                  loginToChat(result)*//*

                //  val body = it.body()!!

*//*
                if (body.data.quick_blox_id == null) {

                    user = newUser
                    *//*
*//*val request = StoreQuickbloxBody(
                        coach_id = loginResponse.data.id.toString(),
                        quickBloxId = loginResponse.data.quick_blox_id.toString()

                    )//


                } else if (newUser.id.toString() == body.data.quick_blox_id) {
                    SharedPrefsHelper.saveQbUser(newUser)
                    loginToChat(result)


                } else {
                    loading.hideProgress()
                    Log.e(
                        "shivam",
                        "response ${body.data.quick_blox_id} newUser${newUser.id} result${result.id}"
                    )

                    //  showToast("Something wrong with quickBloxId")
                }
*//*


            }

            override fun onError(e: QBResponseException) {
                if (e.httpStatusCode == ERROR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                    signInCreatedUser(newUser)
                } else {
                    loading.hideProgress()
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


                *//* SharedPrefsHelper.saveQbUser(user)
                 updateUserOnServer(user)*//*
                // val body = it.body()!!
*//*
                if (body.data.quick_blox_id == null || body.data.quick_blox_id == "null") {


                   // = user
                *//*
*//*    val request = storequickbloxid(

                        quickBloxId = user.id.toString(),
                       user_id = body.data.id.toString()

                    )
                   storequickbloxid(request)*//**//*

                } else if (user.id.toString() == body.data.quick_blox_id) {
                    SharedPrefsHelper.saveQbUser(user)
                    updateUserOnServer(user)
                } else {
                    loading.hideProgress()
                   *//*
*//* this@loginfragment.user = user
                    val request = StoreQuickbloxBody(
                        coach_id = loginResponse.data.id.toString(),
                        quickBloxId = user.id.toString()
                    )
                    loginViewModel.storequickbloxid(request)*//**//*

                    Log.e(
                        "shivam",
                        "response ${body.data.quick_blox_id} newUser${user.id} result${result.id}"
                    )
                    // showToast("Something wrong with quickBloxId_signIncreated_user")
                }
*//*
            }

            override fun onError(responseException: QBResponseException) {
                loading.hideProgress()
                // longToast(R.string.sign_in_error)
            }
        })
    }


    private fun updateUserOnServer(user: QBUser) {
        user.password = null
        QBUsers.updateUser(user).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(updUser: QBUser?, params: Bundle?) {


                *//* SharedPrefsHelper.saveQbUser(user)
                 updateUserOnServer(user)*//*
                *//*     SharedPrefsHelper.saveQbUser(user)
                     updateUserOnServer(user)*//*
                *//* SharedPrefsHelper.saveQbUser(user)
                 updateUserOnServer(user)*//*
                //   loading.hideProgress()
                //  OpponentsActivity.start(this@LoginMainActivity)
                *//* finish()
                  if (cb_login.isChecked)
                      sharedPreference.isLogin = true
                  startactivity(intent(this@loginactivity,homeactivity::class.java))
                  finishAffinity()
                //showLoginUpdateDetailsDialog()


                startActivity(
                    Intent(this@LoginMainActivity, DashboardActivity::class.java)

                )



                finish()*//*

                // viewModel.sendLoginRequest( numberBinding!!.moNumber.text.toString().trim { it <= ' ' }, deviceToken, deviceId, deviceType)
//                 loading.showProgress()

                *//* startActivity(
                     Intent(this, DashboardActivity::class.java)
                             /  OpponentsActivity.start(this@LoginMainActivity)

                 )



                 finish()*//*


            }

            override fun onError(responseException: QBResponseException?) {
                loading.hideProgress()
                // longToast(R.string.update_user_error)
            }
        })
    }


    private fun loginToChat(qbUser: QBUser) {
        val userEmail = Session.getEmailForCreate()
        qbUser.password = Hawk.get(SOCIALID)
        user = qbUser
        startLoginService(qbUser)
    }

    private fun startLoginService(qbUser: QBUser) {

        isStoreapi.value = true


        *//*    val tempIntent = Intent(context, LoginService::class.java)
            val pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
            LoginService.start(this, qbUser, pendingIntent)*//*


    }

*/
    fun saveFcmToken(viewLifecycleOwner: LifecycleOwner, context: Context) {
        viewModelScope.launch {
            fcmTokenResponse.observe(viewLifecycleOwner) {
                if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {

                } else
                    if (it.code() == 401) {
                        val error = Util.error(it.errorBody(), MobileLoginResponse::class.java)

                    }
            }
            fcmTokenResponse.value =
                repository.saveFcmToken()
        }
    }


    fun activateEmailAccount(email: String) {
        viewModelScope.launch {
            emailActivation.value =
                repository.activateEmailAccount(email)

        }
    }

}