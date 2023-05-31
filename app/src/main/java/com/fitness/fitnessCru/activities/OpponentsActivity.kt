package com.fitness.fitnessCru.activities

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.quickbox.adapters.UsersAdapter
import com.fitness.fitnessCru.quickbox.db.QbUsersDbManager
import com.fitness.fitnessCru.quickbox.executor.Executor
import com.fitness.fitnessCru.quickbox.executor.ExecutorTask
import com.fitness.fitnessCru.quickbox.services.CallService
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.*
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.GenericQueryRule
import com.quickblox.core.request.QBPagedRequestBuilder
import com.quickblox.messages.services.QBPushManager
import com.quickblox.messages.services.SubscribeService
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.QBRTCTypes

private const val PER_PAGE_SIZE_100 = 100
private const val ORDER_RULE = "order"
private const val ORDER_DESC_UPDATED = "desc date updated_at"
private const val TOTAL_PAGES_BUNDLE_PARAM = "total_pages"

class OpponentsActivity : BaseActivity() {
    private val TAG = OpponentsActivity::class.java.simpleName

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var loading: CustomProgressLoading
    private lateinit var currentUser: QBUser
    private var currentSession: QBRTCSession? = null

    private var usersAdapter: UsersAdapter? = null

    private var currentPage = 0
    private var isLoading: Boolean = false
    private var hasNextPage: Boolean = true
    private var outt = ""


    companion object {

        fun start(context: Context) {
            val intent = Intent(context, OpponentsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_users)
        loading = CustomProgressLoading(this)

        outt = intent.getIntExtra("logout", 0).toString()

        if (outt == "1") {
            logout()
            //  unsubscribeFromPushes {  }
            //  Toast.makeText(applicationContext, "out", Toast.LENGTH_SHORT).show()
            intent = Intent(this@OpponentsActivity, SplashActivity::class.java)
            intent.putExtra("logout", 1)
            //logoutShared()
            startActivity(intent)
            finishAffinity()

        } else {
            currentSession = WebRtcSessionManager.getCurrentSession()
            //   showToast("Open Opponent")


            //   Log.v("sessionInfoonCreate", currentSession?.userInfo?.get("name").toString())
            /*  val userInfo: Map<String, String> = (session?.userInfo)!!

              val userin = userInfo["name"]
              val userim = userInfo["image"]
              Hawk.put(NAME, userin)
              Hawk.put(IMAGE, userim)*/


            /* val userInfo: HashMap<String, String> = (currentSession?.userInfo as HashMap<String, String>?)!!
             val userin= userInfo.get("name")
             val userim= userInfo.get("image")
             Hawk.put(NAME,userin)
             Hawk.put(IMAGE,userim)*/
            //
            loading.showProgress()
            currentUser = SharedPrefsHelper.getQbUser()
            // showToast("OpponentActivity")

            //   Log.v("sessionInfoonCreate", currentSession?.userInfo?.get("name").toString())

            initDefaultActionBar()
            initUI()
            startLoginService()
            //    showToast("OpponentActivityOnCreate")
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val isIncomingCall = SharedPrefsHelper.get(EXTRA_IS_INCOMING_CALL, false)
        if (isCallServiceRunning(CallService::class.java)) {

//            currentSession = WebRtcSessionManager.getCurrentSession()
//            val userInfo: HashMap<String, String> = (currentSession?.userInfo as HashMap<String, String>?)!!
//          //  showToast("onResumeOpponent")
//
//            val userin = userInfo.get("name")
//            val userim = userInfo.get("image")
//            Hawk.put(NAME, userin)
//            Hawk.put(IMAGE, userim)
//            Log.d(TAG, "CallService is running now")
            CallActivity.start(this, isIncomingCall)
        }
        clearAppNotifications()
        loadUsers()
    }

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

    private fun clearAppNotifications() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun startPermissionsActivity(checkOnlyAudio: Boolean) {
        PermissionsActivity.startForResult(this, checkOnlyAudio, PERMISSIONS)
    }

    private fun loadUsers() {
        isLoading = true
        showProgressDialog(R.string.dlg_loading_opponents)
        currentPage += 1
        val rules = ArrayList<GenericQueryRule>()
        rules.add(GenericQueryRule(ORDER_RULE, ORDER_DESC_UPDATED))
        val requestBuilder = QBPagedRequestBuilder()
        requestBuilder.rules = rules
        requestBuilder.perPage = PER_PAGE_SIZE_100
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
                        usersAdapter?.addUsers(qbUsers)
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

    private fun initUI() {
        usersRecyclerView = findViewById(R.id.list_select_users)
    }

    private fun initOpponents() {
        val opponents = QbUsersDbManager.allUsers
        opponents.remove(SharedPrefsHelper.getQbUser())
        if (usersAdapter == null) {
            usersAdapter = UsersAdapter(this, opponents)
            usersAdapter?.setSelectedItemsCountsChangedListener(object :
                UsersAdapter.SelectedItemsCountsChangedListener {
                override fun onCountSelectedItemsChanged(count: Int) {
                    updateActionBar(count)
                }
            })

            usersRecyclerView.layoutManager = LinearLayoutManager(this)
            usersRecyclerView.adapter = usersAdapter
            usersRecyclerView.addOnScrollListener(ScrollListener(usersRecyclerView.layoutManager as LinearLayoutManager))
        } else {
            usersAdapter?.updateUsersList(opponents)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (usersAdapter != null && usersAdapter!!.selectedUsers.isNotEmpty()) {
            menuInflater.inflate(R.menu.activity_selected_opponents, menu)
        } else {
            menuInflater.inflate(R.menu.activity_opponents, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.update_opponents_list -> {
                currentPage = 0
                loadUsers()
                return true
            }
            R.id.log_out -> {
                unsubscribeFromPushes(callback = {
                    logout()
                })
                return true
            }
            R.id.start_video_call -> {
                if (checkIsLoggedInChat()) {
                    startCall(true)
                }
                if (checkPermissions(PERMISSIONS)) {
                    startPermissionsActivity(false)
                }
                return true
            }
            R.id.start_audio_call -> {
                if (checkIsLoggedInChat()) {
                    startCall(false)
                }
                if (checkPermission(PERMISSIONS[1])) {
                    startPermissionsActivity(true)
                }
                return true
            }
            R.id.appinfo -> {
                AppInfoActivity.start(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun checkIsLoggedInChat(): Boolean {
        if (!QBChatService.getInstance().isLoggedIn) {
            startLoginService()
            shortToast(R.string.login_chat_retry)
            return false
        }
        return true
    }

    private fun startLoginService() {
        if (SharedPrefsHelper.hasQbUser()) {
            LoginService.loginToChatAndInitRTCClient(this, SharedPrefsHelper.getQbUser())
        }
    }

    private fun startCall(isVideoCall: Boolean) {
        val usersCount = usersAdapter?.selectedUsers?.size
        if (usersCount != null && usersCount > MAX_OPPONENTS_COUNT) {
            longToast(
                String.format(
                    getString(R.string.error_max_opponents_count),
                    MAX_OPPONENTS_COUNT
                )
            )
            return
        }

        val userIds = usersAdapter?.selectedUsers?.let { getOpponentIds(it) }
        val conferenceType = if (isVideoCall) {
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
        } else {
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO
        }
        val rtcClient = QBRTCClient.getInstance(applicationContext)
        val session = rtcClient.createNewSessionWithOpponents(userIds, conferenceType)
        WebRtcSessionManager.setCurrentSession(session)

        Log.v("sessionInfoonCreate", session?.userInfo?.get("name").toString())

        // make Users FullName Strings and id's list for iOS VOIP push
        val sessionId = session.sessionID
        val opponentIds = ArrayList<String>()
        val opponentNames = ArrayList<String>()
        val usersInCall = usersAdapter?.selectedUsers as ArrayList<QBUser>

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
                        sendPushMessage(
                            userId,
                            currentUser.fullName,
                            sessionId,
                            idsInLine,
                            namesInLine,
                            isVideoCall
                        )
                    }
                }

                override fun onError(exception: Exception) {
                    shortToast(exception.message.toString())
                }
            })
        }

        CallActivity.start(this, false)
    }

    private fun getOpponentIds(opponents: Collection<QBUser>): ArrayList<Int> {
        val opponentIds = ArrayList<Int>()
        for (qbUser in opponents) {
            opponentIds.add(qbUser.id)
        }
        return opponentIds
    }

    private fun updateActionBar(countSelectedUsers: Int) {
        if (countSelectedUsers < 1) {
            initDefaultActionBar()
        } else {
            val title = if (countSelectedUsers > 1) {
                R.string.tile_many_users_selected
            } else {
                R.string.title_one_user_selected
            }
            supportActionBar?.title = getString(title, countSelectedUsers)
            supportActionBar?.subtitle = null
        }
        invalidateOptionsMenu()
    }

    private fun initDefaultActionBar() {
        val currentUserFullName = SharedPrefsHelper.getQbUser().fullName
        supportActionBar?.title = ""
        supportActionBar?.subtitle =
            getString(R.string.subtitle_text_logged_in_as, currentUserFullName)
    }

    private fun logout() {
        Log.d(TAG, "Removing User data, and Logout")
        LoginService.logoutFromChat(this)
        LoginService.destroyRTCClient(this)
        QBUsers.signOut().performAsync(object : QBEntityCallback<Void> {
            override fun onSuccess(v: Void?, b: Bundle?) {
                removeAllUserData()

            }

            override fun onError(e: QBResponseException) {
                showErrorSnackbar(R.string.dlg_error, e) {
                    unsubscribeFromPushes {
                        logout()
                    }
                }
            }
        })
    }

    private fun unsubscribeFromPushes(callback: () -> Unit) {
        if (QBPushManager.getInstance().isSubscribedToPushes) {
            QBPushManager.getInstance().addListener(SubscribeListener(TAG, callback))
            SubscribeService.unSubscribeFromPushes(this@OpponentsActivity)
        } else {
            callback()
        }
    }

    private fun removeAllUserData() {
        SharedPrefsHelper.clearAllData()
        QbUsersDbManager.clearDB()
        QBUsers.signOut().performAsync(null)
    }

    private inner class ScrollListener(val layoutManager: LinearLayoutManager) :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!isLoading && hasNextPage && dy > 0) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                val needToLoadMore = visibleItemCount * 2 + firstVisibleItem >= totalItemCount
                if (needToLoadMore) {
                    loadUsers()
                }
            }
        }
    }

    private inner class SubscribeListener(val tag: String?, val callback: () -> Unit) :
        QBPushManager.QBSubscribeListener {
        override fun onSubscriptionDeleted(success: Boolean) {
            QBPushManager.getInstance().removeListener(this)
            callback()
        }

        override fun onSubscriptionCreated() {
            // empty
        }

        override fun onSubscriptionError(p0: Exception?, p1: Int) {
            // empty
        }

        override fun equals(other: Any?): Boolean {
            if (other is SubscribeListener) {
                return tag == other.tag
            }
            return false
        }

        override fun hashCode(): Int {
            var hash = 1
            hash = 31 * hash + tag.hashCode()
            return hash
        }
    }
}