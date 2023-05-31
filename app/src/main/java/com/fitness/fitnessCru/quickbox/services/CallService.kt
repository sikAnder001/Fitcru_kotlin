package com.fitness.fitnessCru.quickbox.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ennovations.fitcrutrainer.fragments.CAMERA_ENABLED
import com.ennovations.fitcrutrainer.fragments.IS_CURRENT_CAMERA_FRONT
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.CallActivity
import com.fitness.fitnessCru.fragment.MIC_ENABLED
import com.fitness.fitnessCru.fragment.SPEAKER_ENABLED
import com.fitness.fitnessCru.quickbox.db.QbUsersDbManager
import com.fitness.fitnessCru.quickbox.util.NetworkConnectionChecker
import com.fitness.fitnessCru.quickbox.utils.*
import com.orhanobut.hawk.Hawk
import com.quickblox.chat.QBChatService
import com.quickblox.videochat.webrtc.*
import com.quickblox.videochat.webrtc.BaseSession.QBRTCSessionState
import com.quickblox.videochat.webrtc.callbacks.*
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack
import org.jivesoftware.smack.AbstractConnectionListener
import org.jivesoftware.smack.ConnectionListener
import org.webrtc.CameraVideoCapturer
import java.util.*

const val SERVICE_ID = 787
const val CHANNEL_ID = "Quickblox channel"
const val CHANNEL_NAME = "Quickblox background service"
const val MIN_OPPONENT_SIZE = 1

class CallService : Service() {
    private var TAG = CallService::class.java.simpleName

    private val callServiceBinder: CallServiceBinder = CallServiceBinder()

    private var videoTrackMap: MutableMap<Int, QBRTCVideoTrack> = HashMap()
    private lateinit var networkConnectionListener: NetworkConnectionListener
    private lateinit var networkConnectionChecker: NetworkConnectionChecker
    private lateinit var sessionEventsListener: SessionEventsListener
    private lateinit var connectionListener: ConnectionListenerImpl
    private lateinit var sessionStateListener: SessionStateListener
    private lateinit var signalingListener: QBRTCSignalingListener
    private lateinit var videoTrackListener: VideoTrackListener
    private lateinit var appRTCAudioManager: AppRTCAudioManager
    private var callTimerListener: CallTimerListener? = null
    private var ringtonePlayer: RingtonePlayer? = null
    private var currentSession: QBRTCSession? = null
    private var expirationReconnectionTime: Long = 0
    private var sharingScreenState: Boolean = false
    private var isConnectedCall: Boolean = false
    private lateinit var rtcClient: QBRTCClient


    private val callTimerTask: CallTimerTask = CallTimerTask()
    private var callTime: Long? = null
    private val callTimer = Timer()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CallService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, CallService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        currentSession = WebRtcSessionManager.getCurrentSession()
        clearButtonsState()
        initNetworkChecker()
        initRTCClient()
        initListeners()
        initAudioManager()
        ringtonePlayer = RingtonePlayer(this, com.fitness.fitnessCru.R.raw.beep)
        super.onCreate()

        //val userInfo: Map<String, String> = currentSession?.getUserInfo()!!
        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        Hawk.put(com.fitness.fitnessCru.utility.NAME, userin)

        Log.v("noodles", userInfo.get("name").toString())


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = initNotification()
        startForeground(SERVICE_ID, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnectionChecker.unregisterListener(networkConnectionListener)
        removeConnectionListener(connectionListener)

        releaseCurrentSession()
        releaseAudioManager()

        stopCallTimer()
        clearButtonsState()
        clearCallState()
        stopForeground(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onBind(intent: Intent?): IBinder {
        return callServiceBinder
    }

    @SuppressLint("StringFormatInvalid")
    private fun initNotification(): Notification {
        var intentFlag = 0

        val notifyIntent = Intent(this, CallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intentFlag = PendingIntent.FLAG_IMMUTABLE
        }

        val notifyPendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent,
            intentFlag
        )


        // val outMessage = String.format(R.string.text_push_notification_message.toString(), senderName)

        // val userInfo: Map<String, String> = session?.getUserInfo()!!
        //  username= userInfo.get("name").toString()


        // Hawk.put(com.fitness.fitnessCru.utility.NAME)

        //username=Hawk.get(NAME)

        // val userInfo: Map<String, String> = currentSession?.getUserInfo()!!
        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        Hawk.put(com.fitness.fitnessCru.utility.NAME, userin)


        Log.v("noodles", userInfo.get("name").toString())
        val notificationTitle = getString(R.string.push_notification_message)
        /*

       // var notificationTitle=""



        if(userin!=null){

        }
        else
        {
            val notificationTitle = getString(R.string.text_push_notification_message, userin)
        }
*/


        // var notificationText = getString(com.fitness.fitnessCru.R.string.notification_text, "")

        val callTime = getCallTime()
/*
        if (!TextUtils.isEmpty(callTime)) {
            notificationText =
                getString(com.fitness.fitnessCru.R.string.notification_text, callTime)
        }
*/

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(notificationTitle)
        //  bigTextStyle.bigText(notificationText)

        val channelId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
        } else {
            getString(com.fitness.fitnessCru.R.string.app_name)
        }

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setStyle(bigTextStyle)
        builder.setContentTitle(notificationTitle)
        // builder.setContentText(notificationText)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(com.fitness.fitnessCru.R.drawable.placeholder)/*
        val bitmapIcon =
            BitmapFactory.decodeResource(resources, com.fitness.fitnessCru.R.mipmap.ic_launcher)
        builder.setLargeIcon(bitmapIcon)*/

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_LOW
        } else {
            builder.priority = Notification.PRIORITY_LOW
        }
        builder.apply {
            setContentIntent(notifyPendingIntent)
        }
        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.lightColor = getColor(com.fitness.fitnessCru.R.color.green)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    private fun getCallTime(): String {
        var time = ""
        callTime?.let {
            val format = String.format("%%0%dd", 2)
            val elapsedTime = it / 1000
            val seconds = String.format(format, elapsedTime % 60)
            val minutes = String.format(format, elapsedTime % 3600 / 60)
            val hours = String.format(format, elapsedTime / 3600)
            time = "$minutes:$seconds"
            if (!TextUtils.isEmpty(hours) && hours != "00") {
                time = "$hours:$minutes:$seconds"
            }
        }
        return time
    }

    fun playRingtone() {
        ringtonePlayer?.play(true)
    }

    fun stopRingtone() {
        ringtonePlayer?.stop()
    }

    private fun initNetworkChecker() {
        networkConnectionChecker = NetworkConnectionChecker(application)
        networkConnectionListener = NetworkConnectionListener()
        networkConnectionChecker.registerListener(networkConnectionListener)
    }

    private fun initRTCClient() {
        rtcClient = QBRTCClient.getInstance(this)
        rtcClient.setCameraErrorHandler(CameraEventsListener())

        applyRTCSettings()

        rtcClient.prepareToProcessCalls()
    }

    private fun initListeners() {
        sessionStateListener = SessionStateListener()
        addSessionStateListener(sessionStateListener)

        signalingListener = QBRTCSignalingListener()
        addSignalingListener(signalingListener)

        videoTrackListener = VideoTrackListener()
        addVideoTrackListener(videoTrackListener)

        connectionListener = ConnectionListenerImpl()
        addConnectionListener(connectionListener)

        sessionEventsListener = SessionEventsListener()
        addSessionEventsListener(sessionEventsListener)
    }

    private fun initAudioManager() {
        appRTCAudioManager = AppRTCAudioManager.create(this)

        appRTCAudioManager.setOnWiredHeadsetStateListener { plugged, hasMicrophone ->
            //  shortToast("Headset " + if (plugged) "plugged" else "unplugged")
        }

        appRTCAudioManager.setBluetoothAudioDeviceStateListener { connected ->
            //   shortToast("Bluetooth " + if (connected) "connected" else "disconnected")
        }

        appRTCAudioManager.start { selectedAudioDevice, availableAudioDevices ->
            //  shortToast("Audio device switched to  $selectedAudioDevice")
        }

        if (currentSessionExist() && currentSession!!.conferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO) {
            appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE)
        }
    }

    fun releaseAudioManager() {
        appRTCAudioManager.stop()
    }

    fun currentSessionExist(): Boolean {
        /*   currentSession = WebRtcSessionManager.getCurrentSession()
           val userInfo: HashMap<String, String> = (currentSession?.userInfo as HashMap<String, String>?)!!
           val userin= userInfo.get("name")
           val userim= userInfo.get("image")
           Hawk.put(NAME,userin)
           Hawk.put(IMAGE,userim)*/


        return currentSession != null
    }

    private fun releaseCurrentSession() {
        Log.d(TAG, "Release current session")
        removeSessionStateListener(sessionStateListener)
        removeSignalingListener(signalingListener)
        removeSessionEventsListener(sessionEventsListener)
        removeVideoTrackListener(videoTrackListener)
        currentSession = null
    }

    fun addConnectionListener(connectionListener: ConnectionListener?) {
        QBChatService.getInstance().addConnectionListener(connectionListener)
    }

    fun removeConnectionListener(connectionListener: ConnectionListener?) {
        QBChatService.getInstance().removeConnectionListener(connectionListener)
    }

    fun addSessionStateListener(callback: QBRTCSessionStateCallback<*>?) {
        currentSession?.addSessionCallbacksListener(callback)
    }

    fun removeSessionStateListener(callback: QBRTCSessionStateCallback<*>?) {
        currentSession?.removeSessionCallbacksListener(callback)
    }

    fun addVideoTrackListener(callback: QBRTCClientVideoTracksCallbacks<QBRTCSession>?) {
        currentSession?.addVideoTrackCallbacksListener(callback)
    }

    fun removeVideoTrackListener(callback: QBRTCClientVideoTracksCallbacks<QBRTCSession>?) {
        currentSession?.removeVideoTrackCallbacksListener(callback)
    }

    private fun addSignalingListener(callback: QBRTCSignalingCallback?) {
        currentSession?.addSignalingCallback(callback)
    }

    private fun removeSignalingListener(callback: QBRTCSignalingCallback?) {
        currentSession?.removeSignalingCallback(callback)
    }

    fun addSessionEventsListener(callback: QBRTCSessionEventsCallback?) {
        rtcClient.addSessionCallbacksListener(callback)
    }

    fun removeSessionEventsListener(callback: QBRTCSessionEventsCallback?) {
        rtcClient.removeSessionsCallbacksListener(callback)
    }

    fun acceptCall(userInfo: Map<String, String>) {
        currentSession?.acceptCall(userInfo)
    }

    fun startCall(userInfo: Map<String, String>) {
        currentSession?.startCall(userInfo)
    }

    fun rejectCurrentSession(userInfo: Map<String, String>) {
        currentSession?.rejectCall(userInfo)
    }

    fun hangUpCurrentSession(userInfo: Map<String, String>): Boolean {
        stopRingtone()
        var result = false
        currentSession?.let {
            it.hangUp(userInfo)
            result = true
        }
        return result
    }

    fun setAudioEnabled(enabled: Boolean) {
        currentSession?.mediaStreamManager?.localAudioTrack?.setEnabled(enabled)
    }

    fun startScreenSharing(data: Intent) {
        sharingScreenState = true
        currentSession?.mediaStreamManager?.videoCapturer = QBRTCScreenCapturer(data, null)
    }

    fun stopScreenSharing() {
        sharingScreenState = false
        try {
            currentSession?.mediaStreamManager?.videoCapturer = QBRTCCameraVideoCapturer(this, null)
        } catch (e: QBRTCCameraVideoCapturer.QBRTCCameraCapturerException) {
            Log.i(TAG, "Error: device doesn't have camera")
        }
    }

    fun getCallerId(): Int? {
        return currentSession?.callerID
    }

    fun getOpponents(): List<Int>? {
        return currentSession?.opponents
    }

    fun isVideoCall(): Boolean {
        return QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO == currentSession?.conferenceType
    }

    fun setVideoEnabled(videoEnabled: Boolean) {
        currentSession?.mediaStreamManager?.localVideoTrack?.setEnabled(videoEnabled)
    }

    fun getCurrentSessionState(): BaseSession.QBRTCSessionState? {
        return currentSession?.state
    }

    fun isMediaStreamManagerExist(): Boolean {
        return currentSession?.mediaStreamManager != null
    }

    fun getPeerChannel(userId: Int): QBRTCTypes.QBRTCConnectionState? {
        return currentSession?.getPeerConnection(userId)?.state
    }

    fun isCurrentSession(session: QBRTCSession?): Boolean {
        var isCurrentSession = false
        session?.let {
            isCurrentSession = currentSession?.sessionID == it.sessionID
        }
        return isCurrentSession
    }

    fun switchCamera(cameraSwitchHandler: CameraVideoCapturer.CameraSwitchHandler) {
        val videoCapturer =
            currentSession?.mediaStreamManager?.videoCapturer as QBRTCCameraVideoCapturer
        videoCapturer.switchCamera(cameraSwitchHandler)
    }

    fun switchAudio() {
        Log.v(
            TAG,
            "onSwitchAudio(), SelectedAudioDevice() = " + appRTCAudioManager.selectedAudioDevice
        )
        if (appRTCAudioManager.selectedAudioDevice != AppRTCAudioManager.AudioDevice.SPEAKER_PHONE) {
            appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE)
        } else {
            if (appRTCAudioManager.audioDevices.contains(AppRTCAudioManager.AudioDevice.BLUETOOTH)) {
                appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.BLUETOOTH)
            } else if (appRTCAudioManager.audioDevices.contains(AppRTCAudioManager.AudioDevice.WIRED_HEADSET)) {
                appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET)
            } else {
                appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE)
            }
        }
    }

    fun isSharingScreenState(): Boolean {
        return sharingScreenState
    }

    fun isConnectedCall(): Boolean {
        return isConnectedCall
    }

    fun getVideoTrackMap(): MutableMap<Int, QBRTCVideoTrack> {
        return videoTrackMap
    }

    private fun addVideoTrack(userId: Int, videoTrack: QBRTCVideoTrack) {
        videoTrackMap[userId] = videoTrack
    }

    fun getVideoTrack(userId: Int?): QBRTCVideoTrack? {
        return videoTrackMap[userId]
    }

    private fun removeVideoTrack(userId: Int?) {
        val videoTrack = getVideoTrack(userId)
        val renderer = videoTrack?.renderer
        videoTrack?.removeRenderer(renderer)
        videoTrackMap.remove(userId)
    }

    fun setCallTimerCallback(callback: CallTimerListener) {
        callTimerListener = callback
    }

    fun removeCallTimerCallback() {
        callTimerListener = null
    }

    private fun startCallTimer() {
        if (callTime == null) {
            callTime = 1000
        }
        if (!callTimerTask.isRunning) {
            callTimer.scheduleAtFixedRate(callTimerTask, 0, 1000)
        }
    }

    private fun stopCallTimer() {
        callTimerListener = null

        callTimer.cancel()
        callTimer.purge()
    }

    fun clearButtonsState() {
        SharedPrefsHelper.delete(MIC_ENABLED)
        SharedPrefsHelper.delete(SPEAKER_ENABLED)
        SharedPrefsHelper.delete(CAMERA_ENABLED)
        SharedPrefsHelper.delete(IS_CURRENT_CAMERA_FRONT)
    }

    fun clearCallState() {
        SharedPrefsHelper.delete(EXTRA_IS_INCOMING_CALL)
    }

    private inner class CallTimerTask : TimerTask() {
        var isRunning: Boolean = false

        override fun run() {
            isRunning = true

            callTime = callTime?.plus(1000L)
            val notification = initNotification()
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(SERVICE_ID, notification)

            callTimerListener?.let {
                val callTime = getCallTime()
                if (!TextUtils.isEmpty(callTime)) {
                    it.onCallTimeUpdate(callTime)
                }
            }
        }
    }

    inner class CallServiceBinder : Binder() {
        fun getService(): CallService = this@CallService
    }

    private inner class ConnectionListenerImpl : AbstractConnectionListener() {
        override fun connectionClosedOnError(e: Exception?) {
            val HANG_UP_TIME_10_SECONDS = 10 * 1000
            expirationReconnectionTime = System.currentTimeMillis() + HANG_UP_TIME_10_SECONDS
        }

        override fun reconnectionSuccessful() {
        }

        override fun reconnectingIn(seconds: Int) {
            Log.i(TAG, "reconnectingIn $seconds")
            if (!isConnectedCall && expirationReconnectionTime < System.currentTimeMillis()) {
                hangUpCurrentSession(HashMap())
            }
        }
    }

    private inner class SessionEventsListener : QBRTCClientSessionCallbacks {
        override fun onUserNotAnswer(session: QBRTCSession?, userId: Int?) {
            stopRingtone()
        }

        override fun onSessionStartClose(session: QBRTCSession?) {
            // empty
        }

        override fun onReceiveHangUpFromUser(
            session: QBRTCSession?,
            userID: Int?,
            p2: MutableMap<String, String>?
        ) {
            val userInfo: Map<String, String> = session?.getUserInfo()!!
            val userin = userInfo.get("name")
            Hawk.put(com.fitness.fitnessCru.utility.NAME, userin)

            Log.v("noodles", userInfo.get("name").toString())

            stopRingtone()
            if (session == WebRtcSessionManager.getCurrentSession()) {
                val numberOpponents = session?.opponents?.size
                if (numberOpponents == MIN_OPPONENT_SIZE || session?.state == QBRTCSessionState.QB_RTC_SESSION_PENDING) {
                    if (userID?.equals(session.callerID) == true) {
                        currentSession?.hangUp(HashMap<String, String>())
                    }
                } else {
                    userID?.let {
                        removeVideoTrack(it)
                    }
                }
            }

            val participant = QbUsersDbManager.getUserById(userID)


            /* val participantName =
                  if (participant != null) participant.fullName else userID.toString()


              shortToast("User " + participantName + " " + getString(com.fitness.fitnessCru.R.string.text_status_hang_up) + " conversation")
        }*/
        }

        override fun onCallAcceptByUser(
            session: QBRTCSession?,
            p1: Int?,
            p2: MutableMap<String, String>?
        ) {
            stopRingtone()
            if (session != WebRtcSessionManager.getCurrentSession()) {
                return
            }
        }

        override fun onReceiveNewSession(session: QBRTCSession?) {
            val userInfo: Map<String, String> = session?.getUserInfo()!!
            val userin = userInfo.get("name")
            Hawk.put(com.fitness.fitnessCru.utility.NAME, userin)

            Log.d(TAG, "Session " + session?.sessionID + " are income")
            WebRtcSessionManager.getCurrentSession()?.let {
                Log.d(TAG, "Stop new session. Device now is busy")
                session?.rejectCall(null)
            }
        }

        override fun onUserNoActions(p0: QBRTCSession?, p1: Int?) {
            //  longToast("Call was stopped by UserNoActions timer")
            clearCallState()
            clearButtonsState()
            WebRtcSessionManager.setCurrentSession(null)
            stop(this@CallService)
        }

        override fun onSessionClosed(session: QBRTCSession?) {
            Log.d(TAG, "Session " + session?.sessionID + " start stop session")
            if (session == currentSession) {
                stopRingtone()
                Log.d(TAG, "Stop session")
                stop(this@CallService)
            }
        }

        override fun onCallRejectByUser(
            session: QBRTCSession?,
            p1: Int?,
            p2: MutableMap<String, String>?
        ) {
            stopRingtone()
            if (session != WebRtcSessionManager.getCurrentSession()) {
                return
            }
        }
    }

    private inner class SessionStateListener : QBRTCSessionStateCallback<QBRTCSession> {
        override fun onDisconnectedFromUser(session: QBRTCSession?, userId: Int?) {
            Log.d(TAG, "Disconnected from user: $userId")
        }

        override fun onConnectedToUser(session: QBRTCSession?, userId: Int?) {
            stopRingtone()
            isConnectedCall = true
            Log.d(TAG, "onConnectedToUser() is started")
            startCallTimer()
        }

        override fun onConnectionClosedForUser(session: QBRTCSession?, userID: Int?) {
            Log.d(TAG, "Connection closed for user: $userID")
            //   shortToast("The user: " + userID + "has left the call")
            removeVideoTrack(userID)
        }

        override fun onStateChanged(
            session: QBRTCSession?,
            sessionState: BaseSession.QBRTCSessionState?
        ) {

        }
    }

    private inner class QBRTCSignalingListener : QBRTCSignalingCallback {
        override fun onSuccessSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?) {
            // empty
        }

        override fun onErrorSendingPacket(
            p0: QBSignalingSpec.QBSignalCMD?,
            p1: Int?,
            p2: QBRTCSignalException?
        ) {
            shortToast(com.fitness.fitnessCru.R.string.dlg_signal_error)
        }
    }

    private inner class NetworkConnectionListener :
        NetworkConnectionChecker.OnConnectivityChangedListener {
        override fun connectivityChanged(availableNow: Boolean) {
            // shortToast("Internet connection " + if (availableNow) "available" else " unavailable")
        }
    }

    private inner class CameraEventsListener : CameraVideoCapturer.CameraEventsHandler {
        override fun onCameraError(s: String) {
            //   shortToast("Camera error: $s")
        }

        override fun onCameraDisconnected() {
            //  shortToast("Camera Disconnected: ")
        }

        override fun onCameraFreezed(s: String) {
            //  shortToast("Camera freezed: $s")
            hangUpCurrentSession(HashMap())
        }

        override fun onCameraOpening(s: String) {
            //  shortToast("Camera opening: $s")
        }

        override fun onFirstFrameAvailable() {
            // shortToast("onFirstFrameAvailable: ")
        }

        override fun onCameraClosed() {
            // shortToast("Camera closed: ")
        }
    }

    private inner class VideoTrackListener : QBRTCClientVideoTracksCallbacks<QBRTCSession> {
        override fun onLocalVideoTrackReceive(
            session: QBRTCSession?,
            videoTrack: QBRTCVideoTrack?
        ) {
            videoTrack?.let {
                val userId = QBChatService.getInstance().user.id
                removeVideoTrack(userId)
                addVideoTrack(userId, it)
            }
            Log.d(TAG, "onLocalVideoTrackReceive() run")
        }

        override fun onRemoteVideoTrackReceive(
            session: QBRTCSession?,
            videoTrack: QBRTCVideoTrack?,
            userId: Int?
        ) {
            if (videoTrack != null && userId != null) {
                addVideoTrack(userId, videoTrack)
            }
            Log.d(TAG, "onRemoteVideoTrackReceive for opponent= $userId")
        }
    }

    interface CallTimerListener {
        fun onCallTimeUpdate(time: String)
    }
}

