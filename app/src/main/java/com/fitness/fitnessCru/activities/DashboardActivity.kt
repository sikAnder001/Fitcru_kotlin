package com.fitness.fitnessCru.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quickblox.auth.session.QBSettings
import com.quickblox.messages.services.QBPushManager

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController

    private var coaching = ""
    private var studio = ""

    //google fit
    /* private var fitnessOptions: FitnessOptions? = null
     protected val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 11
     private var totSteps = ""
     private var totCal = ""*/

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        navController = findNavController(R.id.fragment_container)


        setBottomNavigation()

        binding.bottomNavigation.itemIconTintList = null

        coaching = intent.getStringExtra("coaching").toString()
        if (coaching == "done") {
            navController.navigate(R.id.coaching)
        }
        studio = intent.getStringExtra("studio").toString()
        if (studio == "done") {
            val bundle = Bundle()
            bundle.putInt("position", 1)
            navController.navigate(R.id.workout, bundle)
        } else if (studio == "program") {
            val bundle = Bundle()
            bundle.putInt("position", 0)
            navController.navigate(R.id.workout, bundle)
        }



        QBSettings.getInstance().isEnablePushNotification = true


        /* fitnessOptions = FitnessOptions.builder()
             .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
             .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
             .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
             .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
             .build()
         val accountGoogleFit =
             GoogleSignIn.getAccountForExtension(this, fitnessOptions!!)

         if (!GoogleSignIn.hasPermissions(accountGoogleFit, fitnessOptions!!)) {
             GoogleSignIn.requestPermissions(
                 this, // your activity
                 GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                 accountGoogleFit,
                 fitnessOptions!!
             )
         } else {
             accessGoogleFit()
         }*/


        QBPushManager.getInstance().addListener(object : QBPushManager.QBSubscribeListener {
            override fun onSubscriptionCreated() {
                Log.v("onsubscribe", "onSubscriptionCreated")
            }

            override fun onSubscriptionError(p0: Exception?, p1: Int) {
                Log.v("subscribe", "onSubscriptionError")
            }

            override fun onSubscriptionDeleted(p0: Boolean) {
                Log.v("unsubscribe", "onSubscriptionDeleted")
            }


        })


    }

    private fun setBottomNavigation() {
        val navController = findNavController(R.id.fragment_container)


        findViewById<BottomNavigationView>(R.id.bottom_navigation)

            .setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.home2 -> {
                    showBottomNav()
                }

                R.id.workout -> showBottomNav()

                R.id.nutrition -> showBottomNav()

                R.id.coaching -> showBottomNav()

                R.id.chat -> showBottomNav()

                R.id.coaching3 -> {
                    showBottomNav()
                    binding.bottomNavigation.menu.setGroupCheckable(0, true, false)
                    for (i in 0 until binding.bottomNavigation.menu.size()) {
                        binding.bottomNavigation.menu.getItem(i).isChecked = false

                    }
                    binding.bottomNavigation.menu.setGroupCheckable(0, true, true)
                }

                else -> hideBottomNav()
            }
        }


    }


/*

    private fun isCallServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(Integer.MAX_VALUE)
        for (service in services) {
            if (CallService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    internal fun showProgressDialog(@StringRes messageId: Int) {
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
    }

    private fun clearAppNotifications() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }*/
/*
    private fun loadUsers() {
        isLoading = true
        showProgressDialog(R.string.dlg_loading_opponents)
        currentPage += 1
        val rules = ArrayList<GenericQueryRule>()
        rules.add(
            GenericQueryRule(
                ORDER_RULE,
                ORDER_DESC_UPDATED
            )
        )
        val requestBuilder = QBPagedRequestBuilder()
        requestBuilder.rules = rules
        requestBuilder.perPage =PER_PAGE_SIZE_100
        requestBuilder.page = currentPage

        QBUsers.getUsers(requestBuilder).performAsync(object : QBEntityCallback<ArrayList<QBUser>> {
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, bundle: Bundle?) {
                qbUsers?.let {
                    Log.d(TAG, "Successfully loaded users")
                    QbUsersDbManager.saveAllUsers(qbUsers, true)

                    val totalPagesFromParams = bundle?.get(TOTAL_PAGES_BUNDLE_PARAM) as Int
                    if (currentPage >= totalPagesFromParams) {
                        hasNextPage = false
                    }

                    if (currentPage == 1) {
                        initOpponents()
                    } else {
                        //usersAdapter?.addUsers(qbUsers)
                    }
                }
                hideProgressDialog()
                isLoading = false
            }

            override fun onError(e: QBResponseException?) {
                e?.let {
                    Log.d(TAG, "Error load users" + e.message)
                    hideProgressDialog()
                    isLoading = false
                    currentPage -= 1
                    showErrorSnackbar(R.string.loading_users_error, e) {
                        loadUsers()
                    }
                }
            }
        })
    }
*/

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE
    }
/*

    protected fun showErrorSnackbar(@StringRes resId: Int, e: Exception?, clickListener: View.OnClickListener) {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        com.fitness.fitnessCru.quickbox.utils.showErrorSnackbar(
            rootView,
            resId,
            e,
            R.string.dlg_retry,
            clickListener
        )
    }

    private fun initOpponents() {
        opponents = QbUsersDbManager.allUsers
        opponents.remove(SharedPrefsHelper.getCurrentUser())
        *//*  if (usersAdapter == null) {
              usersAdapter = UsersAdapter(requireActivity(), opponents)
              usersAdapter?.setSelectedItemsCountsChangedListener(object :
                  UsersAdapter.SelectedItemsCountsChangedListener {
                  override fun onCountSelectedItemsChanged(count: Int) {

                  }
              })

              usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
              usersRecyclerView.adapter = usersAdapter
              usersRecyclerView.addOnScrollListener(ScrollListener(usersRecyclerView.layoutManager as LinearLayoutManager))
          } else {
              usersAdapter?.updateUsersList(opponents)
          }*//*
    }





*//*
    private fun startCall(isVideoCall: Boolean,clientdata: ArrayList<QBUser>) {
        // val usersCount = usersAdapter?.selectedUsers?.size
        *//*
*//* if (usersCount != null && usersCount > MAX_OPPONENTS_COUNT) {
             longToast(String.format(getString(R.string.error_max_opponents_count), MAX_OPPONENTS_COUNT))
             return
         }*//**//*


        val userIds = clientdata.map { it.id }
        val conferenceType = if (isVideoCall) {
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
        } else {
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO
        }
        val rtcClient = QBRTCClient.getInstance(requireContext())
        val session = rtcClient.createNewSessionWithOpponents(userIds, conferenceType)
        WebRtcSessionManager.setCurrentSession(session)

        // make Users FullName Strings and id's list for iOS VOIP push
        val sessionId = session.sessionID
        val opponentIds = ArrayList<String>()
        val opponentNames = ArrayList<String>()
        val usersInCall = clientdata

        // the Caller in exactly first position is needed regarding to iOS 13 functionality
        usersInCall.add(0, currentUser)

        for (user in usersInCall) {
            val userId = user.id.toString()
            val name = user.fullName ?: user.login

            opponentIds.add(userId)
            opponentNames.add(name)
        }

        val idsInLine = TextUtils.join(",", opponentIds)
        val namesInLine = TextUtils.join(",", opponentNames)

        Log.d(TAG, "New Session with id: $sessionId\n Users in Call: \n$idsInLine\n$namesInLine")

        userIds?.forEach { userId ->
            Executor.addTask(object : ExecutorTask<Boolean> {
                override fun onBackground(): Boolean {
                    val timeout3Seconds = 3000L
                    return QBChatService.getInstance().pingManager.pingUser(userId, timeout3Seconds)
                }

                override fun onForeground(result: Boolean) {
                    if (result) {
                        val message =
                            "Participant with id: $userId is online. There is no need to send a VoIP notification."
                        Log.d(TAG, message)
                    } else {
                        sendPushMessage(userId, currentUser.login, sessionId, idsInLine, namesInLine, isVideoCall)
                    }
                }

                override fun onError(exception: Exception) {
                    shortToast(exception.message.toString())
                }
            })
        }

        CallActivity.start(requireContext(), false)
    }
*//*

    private fun checkIsLoggedInChat(): Boolean {
        if (!QBChatService.getInstance().isLoggedIn) {
            startLoginService()
            shortToast(R.string.login_chat_retry)
            return false
        }
        return true
    }

    protected fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (checkPermission(permission)) {
                return true
            }
        }
        return false
    }

    private fun startLoginService() {
        if (SharedPrefsHelper.hasCurrentUser()) {
            LoginService.loginToChatAndInitRTCClient(this, SharedPrefsHelper.getCurrentUser())
        }
    }


    protected fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED
    }*/


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun accessGoogleFit() {
//        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
//        val startTime = endTime.minusDays(1)
//
//        Log.v("timingNow:","....${LocalDateTime.now()}")
////        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
////        val milliSecondsUntilTomorrow: Long =
////            Duration(now, now.plusDays(1).withTimeAtStartOfDay()).getMillis()
////        val cal: Calendar = Calendar.getInstance()
////        val now = Date()
////        cal.setTime(now)
////        val endTime: Long = cal.getTimeInMillis()
////        cal.add(Calendar.DAY_OF_YEAR, -1)
////        val startTime: Long = cal.getTimeInMillis()
////        Log.i("accessGoogleFit", "Range Start: $startTime  $endTime")
//        val readRequest =
//            DataReadRequest.Builder()
//                // The data request can specify multiple data types to return,
//                // effectively combining multiple data queries into one call.
//                // This example demonstrates aggregating only one data type.
//                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
//                // Analogous to a "Group By" in SQL, defines how data should be
//                // aggregated.
//                // bucketByTime allows for a time span, whereas bucketBySession allows
//                // bucketing by <a href="/fit/android/using-sessions">sessions</a>.
//                .bucketByTime(1, TimeUnit.DAYS)
//                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
//                .build()
////        val account = GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!)
//
//        val readRequest2 = DataReadRequest.Builder()
//            .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
//            .bucketByActivityType(1, TimeUnit.DAYS)
//            .setTimeRange(endTime.minusDays(2).toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
//            .build()
//
////        val datasource = DataSource.Builder()
////            .setAppPackageName("com.google.android.gms")
////            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
////            .setType(DataSource.TYPE_DERIVED)
////            .setStreamName("estimated_steps")
////            .build()
////
////        /*val datasource2 = DataSource.Builder()
////            .setAppPackageName("com.google.calories.expended")
////            .setDataType( DataType.AGGREGATE_CALORIES_EXPENDED)
////            .setType(DataSource.TYPE_RAW)
////            .build()
////*/
////
////
////        val request = DataReadRequest.Builder()
////            .aggregate(datasource)
////            .bucketByTime(1, TimeUnit.DAYS)
////            .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
////            .build()
////
////
////
////        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!))
////            .readData(request)
////            .addOnSuccessListener { response ->
////                val totalSteps = response.buckets
////                    .flatMap { it.dataSets }
////                    .flatMap { it.dataPoints }
////                    .sumBy { it.getValue(Field.FIELD_STEPS).asInt() }
////                Log.i("Field", "ttotal steps: $totalSteps ,,, ")
////
////                    updateRowData(totalSteps.toString())
////            }
//
////        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!))
////            .readData(request2)
////            .addOnSuccessListener { response ->
////                val totalCal = response.buckets
////                    .flatMap { it.dataSets }
////                    .flatMap { it.dataPoints }
////                    .sumBy { it.getValue(Field.FIELD_CALORIES).asInt() }
////                Log.i("Field", "ttotal steps:  ,,, ${response.buckets.flatMap { it.dataSets }}")
////
////            }
//
//
//        Fitness.getHistoryClient(
//            this,
//            GoogleSignIn.getAccountForExtension(this, fitnessOptions!!)
//        )
//            .readData(readRequest)
//            .addOnSuccessListener { response ->
//                // The aggregate query puts datasets into buckets, so flatten into a
//                // single list of datasets
//                for (dataSet in response.buckets.flatMap { it.dataSets }) {
//                    dumpDataSet(dataSet)
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w("accessGoogleFit1", "There was an error reading data from Google Fit", e)
//            }
//
//        Fitness.getHistoryClient(
//            this,
//            GoogleSignIn.getAccountForExtension(this, fitnessOptions!!)
//        )
//            .readData(readRequest2)
//            .addOnSuccessListener { response ->
//                // The aggregate query puts datasets into buckets, so flatten into a
//                // single list of datasets
//                for (dataSet in response.buckets.flatMap { it.dataSets }) {
//                    dumpDataSet2(dataSet)
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "There was an error reading data from Google Fit", e)
//            }
//
//
//
//    }
//
//    fun dumpDataSet2(dataSet: DataSet) {
//        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
//        Log.i(TAG, "dataset2: ${dataSet}")
//        for (dp in dataSet.dataPoints) {
//            Log.i(TAG, "Data point:")
//            Log.i(TAG, "\tType: ${dp.dataType.name}")
//            Log.i(TAG, "\tStart: ${dp.getStartTimeString()}")
//            Log.i(TAG, "\tEnd: ${dp.getEndTimeString()}")
//            for (field in dp.dataType.fields) {
//                Log.i(TAG, "\tField: ${field.name.toString()} Value: ${dp.getValue(field)}")
//                totCal=dp.getValue(field).toString()
//                Session.setSteps(dp.getValue(field).toString())
//            }
//        }
//    }
//
//    fun dumpDataSet(dataSet: DataSet) {
//        Log.i("dataSet.dataType.name", "Data returned for Data type: ${dataSet.dataType.name}")
//        Log.v(
//            "new changeNew change: ",
//            dataSet.dataPoints[0].getValue(Field.FIELD_STEPS).asInt().toString()
//        )
//        for (dp in dataSet.dataPoints) {
//            Log.i("Data point", "Data point:")
//            Log.i("dataType.name", "\tType: ${dp.dataType.name}")
//            Log.i("dp.getStartTimeString()", "\tStart: ${dp.getStartTimeString()}")
//            Log.i("getEndTimeString()", "\tEnd: ${dp.getEndTimeString()}")
//            for (field in dp.dataType.fields) {
//                totSteps = dp.getValue(field).toString()
//                Session.setSteps(dp.getValue(field).toString())
//                Log.i("dp.getValue(field)", "\tField: ${field.name.toString()} Value: ${dp.getValue(field)}")
//
//                Session.setCal(dp.getValue(field).toString())
//
//            }
//        }
//
////        updateRowData()
//    }
//
//    @SuppressLint("NewApi")
//    fun DataPoint.getStartTimeString() = Instant.ofEpochSecond(this.getStartTime(TimeUnit.SECONDS))
//        .atZone(ZoneId.systemDefault())
//        .toLocalDateTime().toString()
//
//    @SuppressLint("NewApi")
//    fun DataPoint.getEndTimeString() = Instant.ofEpochSecond(this.getEndTime(TimeUnit.SECONDS))
//        .atZone(ZoneId.systemDefault())
//        .toLocalDateTime().toString()


}

