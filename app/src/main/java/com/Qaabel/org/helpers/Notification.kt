package com.Qaabel.org.helpers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.Qaabel.org.R
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.view.activity.MainActivity
import kotlin.random.Random

class Notification(var context: Context, var title: String, var message: String, var NotificationType: String, user: FriendModel?) {

    var notificationSound: Uri
    val notifyPendingIntent:PendingIntent

    init {
       notificationSound =RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION)


        var intent=Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        }
        intent.putExtra("Notification","TRUE")
        intent.putExtra("FRIEND_USER",user)
        intent.putExtra("NotificationType",NotificationType)

         notifyPendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )





    }
    fun sendNotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            sendNotificationApi26()
        else
            sendNotificationLowSdk()
    }

    private fun sendNotificationLowSdk() {
        var largerIcon=BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher)
        var builder=NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(largerIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent)
                .setSound(notificationSound)

        var notification=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
       notification.notify(Random.nextInt(),builder.build())
    }

    private fun sendNotificationApi26() {
        val helper=NotificationHelper(context)
        val builder=helper.getQaabelNotification(title,message,notificationSound,notifyPendingIntent)
        helper.getManager().notify(Random.nextInt(),builder.build())
    }



}