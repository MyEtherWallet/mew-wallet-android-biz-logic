package com.myetherwallet.mewwalletbl.connection

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.myetherwallet.mewwalletbl.R

/**
 * Created by BArtWell on 23.07.2019.
 */

private const val NOTIFICATION_ID = 1

class ServiceNotificationHelper {

    fun create(context: Context): Notification? {
        val channelId = "com.myetherwallet.mewwalletbl.connection.socket_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.service_notification_channel_name)
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationChannel.description = context.getString(R.string.service_notification_channel_description)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }

        val action = NotificationCompat.Action.Builder(0, context.getString(R.string.service_notification_disconnect), getServicePendingIntent(context))
            .build()

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        return notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.service_notification)
            .setColor(ContextCompat.getColor(context, R.color.service_notification_icon_background))
            .setChannelId(channelId)
            .setContentTitle(context.getString(R.string.service_notification_title))
            .setContentText(context.getString(R.string.service_notification_text))
            .setContentIntent(getActivityPendingIntent(context))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .addAction(action)
            .build()
    }

    private fun getActivityPendingIntent(context: Context): PendingIntent {
        val activityClass = findActivityClass(context)
        val intent = Intent(context, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun findActivityClass(context: Context): Class<*>? {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!
        val className = launchIntent.component!!.className
        return Class.forName(className)
    }

    private fun getServicePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java)
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
