package net.common.commonlib.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import net.common.commonlib.R

object NotificationUtils {

    lateinit var builder: NotificationCompat.Builder

    private fun getNotiManager(context: Context) : NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, channelDesc: String, importance: Int) {
        var channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDesc
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        getNotiManager(context).createNotificationChannel(channel)
    }

    /**
     * Head-up Notification 세팅
     */
    fun setHeadsUpNotification(context: Context, notiId: Int, channelId: String, intent: PendingIntent, contentView: RemoteViews) {
        setHeadsUpNotification(context, notiId, channelId, intent, contentView, true)
    }

    fun setHeadsUpNotification(context: Context, notiId: Int, channelId: String, intent: PendingIntent, contentView: RemoteViews, isAutoCancel: Boolean) {
        builder = NotificationCompat.Builder(context, channelId).apply {
            priority = NotificationCompat.PRIORITY_HIGH
            setSmallIcon(R.mipmap.ic_launcher)
            setCustomContentView(contentView)
            setCustomHeadsUpContentView(contentView)
            setContentIntent(intent)
            setAutoCancel(isAutoCancel)
            setDefaults(NotificationCompat.DEFAULT_ALL)
        }

        getNotiManager(context).notify(notiId, builder.build())
    }

    /**
     * CustomView Notification 세팅
     */
    fun setCustomNotificaion(context: Context, notiId: Int, channelId: String, intent: PendingIntent, contentView: RemoteViews) {
        setCustomNotificaion(context, notiId, channelId, intent, contentView, true)
    }

    fun setCustomNotificaion(context: Context, notiId: Int, channelId: String, intent: PendingIntent, contentView: RemoteViews, isAutoCancel: Boolean) {
        builder = NotificationCompat.Builder(context, channelId).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT
            setSmallIcon(R.mipmap.ic_launcher)
            setCustomContentView(contentView)
            setContentIntent(intent)
            setAutoCancel(isAutoCancel)
        }

        getNotiManager(context).notify(notiId, builder.build())
    }

    /**
     * Default Notification 세팅
     */
    fun setNotification(context: Context, notiId: Int, channelId: String, title: String, message: String, intent: PendingIntent) {
        setNotification(context, notiId, channelId, title, message, intent, true)
    }

    fun setNotification(context: Context, notiId: Int, channelId: String, title: String, message: String, intent: PendingIntent, isAutoCancel: Boolean) {
        builder = NotificationCompat.Builder(context, channelId).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(title)
            setContentText(message)
            setContentIntent(intent)
            setAutoCancel(isAutoCancel)
        }

        getNotiManager(context).notify(notiId, builder.build())
    }

    /**
     * Notification Update
     */
    fun updateNotificaion(contenxt: Context, notiId: Int, message: String) {
        builder?.let {
            it.setContentText(message)
            getNotiManager(contenxt).notify(notiId, it.build())
        }
    }

    fun clearNotificaion(context: Context, notiId: Int) {
        getNotiManager(context).cancel(notiId)
    }

    fun clearAllNotificaion(context: Context) {
        getNotiManager(context).cancelAll()
    }
}