package com.fitness.fitnessCru.helper

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fitness.fitnessCru.activities.MainActivity


class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "SAMPLE_CHANNEL"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent!!.getIntExtra("notificationId", 0)
        val title = intent!!.getStringExtra("title")
        val message = intent!!.getStringExtra("subTitle")

        val mainIntent = Intent(context, MainActivity::class.java)
        val contentIntent =
            PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val myNotificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel_name: CharSequence = "My Notification"

        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, channel_name, importance)

        myNotificationManager.createNotificationChannel(channel)

        // Prepare notification.
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        // Notify

        // Notify
        myNotificationManager.notify(notificationId, builder.build())
    }

}
