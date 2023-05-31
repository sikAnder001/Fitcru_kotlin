package com.fitness.fitnessCru.quickbox.utils

import com.fitness.fitnessCru.R
import com.quickblox.core.helper.StringifyArrayList
import com.quickblox.messages.QBPushNotifications
import com.quickblox.messages.model.QBEnvironment
import com.quickblox.messages.model.QBEvent
import com.quickblox.messages.model.QBNotificationType
import org.json.JSONException
import org.json.JSONObject

fun sendPushMessage(
    userId: Int, senderName: String, sessionId: String,
    opponentIds: String, opponentNames: String, isVideoCall: Boolean,


    ) {
    /* val userInfo: Map<String, String> =session?.getUserInfo()!!
     userInfo.get("name")*/
    /* val userInfo: Map<String, String> = Session.getUserInfo()!!
     val userin= userInfo.get("name")
     Hawk.put(com.fitness.fitnessCru.utility.NAME,userin)*/

    // Log.v(NAME,userInfo.get("name").toString())

    val outMessage = String.format(R.string.text_push_notification_message.toString(), senderName)


    // send Push: create QuickBlox Push Notification Event
    val qbEvent = QBEvent()
    qbEvent.notificationType = QBNotificationType.PUSH
    qbEvent.environment = QBEnvironment.DEVELOPMENT

    // generic push - will be delivered to all platforms (Android, iOS, WP, Blackberry..)

    val json = JSONObject()
    try {
        json.put("message", outMessage)
        json.put("ios_voip", "1")
        json.put("VOIPCall", "1")
        json.put("sessionID", sessionId)
        json.put("opponentsIDs", opponentIds)
        json.put("contactIdentifier", opponentNames)
        json.put("conferenceType", if (isVideoCall) "1" else "2")
        json.put("timestamp", System.currentTimeMillis().toString())
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    qbEvent.message = json.toString()

    val userIds = StringifyArrayList<Int>()
    userIds.add(userId)
    qbEvent.userIds = userIds

    QBPushNotifications.createEvent(qbEvent).performAsync(null)
}
