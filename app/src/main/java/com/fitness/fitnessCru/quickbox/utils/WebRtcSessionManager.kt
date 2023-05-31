package com.fitness.fitnessCru.quickbox.utils

import android.util.Log
import com.fitness.fitnessCru.activities.CallActivity
import com.fitness.fitnessCru.utility.Session
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl

object WebRtcSessionManager : QBRTCClientSessionCallbacksImpl() {
    private val TAG = WebRtcSessionManager::class.java.simpleName

    private var currentSession: QBRTCSession? = null

    fun getCurrentSession(): QBRTCSession? {
        return currentSession
    }

    fun setCurrentSession(qbCurrentSession: QBRTCSession?) {

        currentSession = qbCurrentSession
    }

    override fun onReceiveNewSession(session: QBRTCSession) {
        Log.d(TAG, "onReceiveNewSession to WebRtcSessionManager")

//longToast("WebRTCSessionManager")
        /*val userInfo: HashMap<String, String> = (session?.userInfo as HashMap<String, String>?)!!
                  val userin= userInfo.get("name")
                  val userim= userInfo.get("image")
                  Hawk.put(NAME,userin)
                  Hawk.put(IMAGE,userim)*/


        if (currentSession == null) {
            setCurrentSession(session)

            session.userInfo
            CallActivity.start(Session.getInstance(), true)
        }
    }

    override fun onSessionClosed(session: QBRTCSession?) {


        if (session == getCurrentSession()) {
            setCurrentSession(null)
        }
    }
}