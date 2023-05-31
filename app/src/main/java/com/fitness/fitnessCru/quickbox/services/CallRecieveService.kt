package com.fitness.fitnessCru.quickbox.services

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.ProgressDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import androidx.annotation.StringRes
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.CallActivity
import com.fitness.fitnessCru.activities.OpponentsActivity
import com.fitness.fitnessCru.quickbox.db.QbUsersDbManager
import com.fitness.fitnessCru.quickbox.utils.EXTRA_IS_INCOMING_CALL
import com.fitness.fitnessCru.quickbox.utils.SharedPrefsHelper
import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.GenericQueryRule
import com.quickblox.core.request.QBPagedRequestBuilder
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser


private const val PER_PAGE_SIZE_100 = 100
private const val ORDER_RULE = "order"
private const val ORDER_DESC_UPDATED = "desc date updated_at"
private const val TOTAL_PAGES_BUNDLE_PARAM = "total_pages"

class CallRecieveService : Service() {

    private val TAG = OpponentsActivity::class.java.simpleName

    private lateinit var currentUser: QBUser

    // private var usersAdapter: UsersAdapter? = null

    private var currentPage = 0
    private var isLoading: Boolean = false
    private var hasNextPage: Boolean = true
    private var progressDialog: ProgressDialog? = null


    override fun onBind(intent: Intent?): IBinder? {
        currentUser = SharedPrefsHelper.getQbUser()
        startLoginService()


        val isIncomingCall = SharedPrefsHelper.get(EXTRA_IS_INCOMING_CALL, false)
        if (isCallServiceRunning(CallService::class.java)) {
            Log.d(TAG, "CallService is running now")
            CallActivity.start(this, isIncomingCall)
        }
        clearAppNotifications()
        loadUsers()
        checkIsLoggedInChat()
        throw UnsupportedOperationException("Not yet implemented")
    }


    private fun initOpponents() {
        val opponents = QbUsersDbManager.allUsers
        opponents.remove(SharedPrefsHelper.getQbUser())
        /*if (usersAdapter == null) {
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
        }*/
    }

    private fun checkIsLoggedInChat(): Boolean {
        if (!QBChatService.getInstance().isLoggedIn) {
            startLoginService()
            // shortToast(R.string.login_chat_retry)
            return false
        }
        return true
    }

    private fun startLoginService() {
        if (SharedPrefsHelper.hasQbUser()) {
            LoginService.loginToChatAndInitRTCClient(this, SharedPrefsHelper.getQbUser())
        }
    }

/*  private fun unsubscribeFromPushes(callback: () -> Unit) {
    if (QBPushManager.getInstance().isSubscribedToPushes) {
      QBPushManager.getInstance().addListener(SubscribeListener(TAG, callback))
      SubscribeService.unSubscribeFromPushes(this@OpponentsActivity)
    } else {
      callback()
    }
  }*/

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
                        // usersAdapter?.addUsers(qbUsers)
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
                    /* showErrorSnackbar(R.string.loading_users_error, e) {
                       loadUsers()
                     }*/
                }
            }
        })
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

    /* protected fun showErrorSnackbar(
       @StringRes resId: Int,
       e: Exception?,
       clickListener: View.OnClickListener
     ) {
       val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
       showErrorSnackbar(rootView, resId, e, R.string.dlg_retry, clickListener)
     }*/

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
}








