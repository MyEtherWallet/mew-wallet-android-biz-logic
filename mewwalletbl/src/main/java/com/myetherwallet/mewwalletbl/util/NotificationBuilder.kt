package com.myetherwallet.mewwalletbl.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Created by BArtWell on 25.02.2022.
 */

class NotificationBuilder {

    private var notificationId: Int? = null
    private var channelId: String? = null
    private var channelName: String? = null
    private var channelDescription: String? = null
    private var title: String? = null
    private var text: String? = null
    private var notificationColor: Int? = null
    private var smallIcon: Int? = null
    private var contentIntent: PendingIntent? = null
    private val actions = mutableListOf<NotificationCompat.Action>()

    fun setNotificationId(notificationId: Int) = apply { this.notificationId = notificationId }

    fun setChannelId(channelId: String) = apply { this.channelId = channelId }

    fun setChannelName(context: Context, @StringRes channelName: Int) = setChannelName(context.getString(channelName))

    fun setChannelName(channelName: String) = apply { this.channelName = channelName }

    fun setChannelDescription(context: Context, @StringRes channelDescription: Int) = setChannelDescription(context.getString(channelDescription))

    fun setChannelDescription(channelDescription: String) = apply { this.channelDescription = channelDescription }

    fun setTitle(context: Context, @StringRes title: Int) = setTitle(context.getString(title))

    fun setTitle(title: String) = apply { this.title = title }

    fun setText(context: Context, @StringRes text: Int) = setText(context.getString(text))

    fun setText(text: String) = apply { this.text = text }

    fun setColor(context: Context, @ColorRes color: Int) = setColor(ContextCompat.getColor(context, color))

    fun setColor(@ColorInt color: Int) = apply { this.notificationColor = color }

    fun setSmallIcon(@DrawableRes smallIcon: Int) = apply { this.smallIcon = smallIcon }

    fun setContentIntentToLauncherActivity(context: Context) = apply {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun addAction(context: Context, @StringRes text: Int, intent: Intent) = addAction(context, context.getString(text), intent)

    fun addAction(context: Context, text: String, intent: Intent) = apply {
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val action = NotificationCompat.Action.Builder(0, text, pendingIntent).build()
        this.actions.add(action)
    }

    fun build(context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationChannel.description = channelDescription
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId!!)
        notificationBuilder.setOngoing(true).apply {
            setSmallIcon(smallIcon!!)
            color = notificationColor!!
            setChannelId(channelId!!)
            setContentTitle(title!!)
            setContentText(text!!)
            contentIntent?.let {
                setContentIntent(it)
            }
            priority = NotificationCompat.PRIORITY_MIN
            setCategory(Notification.CATEGORY_SERVICE)
            for (action in actions) {
                addAction(action)
            }
        }
        return notificationBuilder.build()
    }
}
