package com.fitness.fitnessCru.quickbox.services.fcm

import android.util.Log
import com.fitness.fitnessCru.quickbox.services.LoginService
import com.fitness.fitnessCru.quickbox.utils.SharedPrefsHelper
import com.google.firebase.messaging.RemoteMessage
import com.quickblox.messages.services.fcm.QBFcmPushListenerService
import com.quickblox.users.model.QBUser

class PushListenerService : QBFcmPushListenerService() {
    private val TAG = PushListenerService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        if (SharedPrefsHelper.hasQbUser()) {
            val user: QBUser = SharedPrefsHelper.getQbUser()
            Log.d(TAG, "App has logged user" + user.id)
            LoginService.loginToChatAndInitRTCClient(this, user)
        }

        // val outMessage = String.format(R.string.text_push_notification_message.toString(), Hawk.get(NAME))
        /*  remoteMessage?.from
          remoteMessage?.data*/


    }

    override fun sendPushMessage(data: MutableMap<Any?, Any?>?, from: String?, message: String?) {
        super.sendPushMessage(data, from, message)


        Log.v(TAG, "From: $from")
        Log.v(TAG, "Message: $message")
    }
}