package com.lumen.bikeme.commons.notification

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lumen.bikeme.MainActivity
import com.lumen.bikeme.R

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        println("KITKA NOWE POWIADOMIENIE $message")

        val notifIntent = Intent(this, MainActivity::class.java).apply {}
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifIntent,
            flags
        )

        val channelIdentification = getString(R.string.default_notification_channel_id)

        val notification = NotificationCompat.Builder(this, channelIdentification)
            .setSmallIcon(R.drawable.ic_baseline_cruelty_free_24)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message.notification?.body)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .build()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(101, notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("KITKA NOWY TOKEN $token")
        // cFIkFQr5TqmL6YqrYdkxFm:APA91bHcZzrcyOS4TcMtxLlo5c2WpLoFmy6R8ten39jm0M5tlocvRjFc2NAd3lJNcf1sd8gmrIsxD33Hyn-bgIb7iAs8R_5iB0w9__1UuvsgOzFLePRaIPvFxvCO70-RWLUJ48MztT1l
    }

}