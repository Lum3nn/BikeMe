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

        val notifyIntent = Intent(this, MainActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
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
            notify(101, notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // odbierma token zwiazany z notyfikacjami,
        // po nim moge targetowac wiadomosci do konkretnego urzadzenia
    }
}