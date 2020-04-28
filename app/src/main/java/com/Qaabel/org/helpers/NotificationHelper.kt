package com.Qaabel.org.helpers

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import com.Qaabel.org.R

class NotificationHelper(context: Context): ContextWrapper(context) {
    private final val QAABEL_CHANNEL_ID="com.Qaabel.org"
    private final val QAABEL_CHANNEL_NAME="Qaabel"

    private  var notificationManager:NotificationManager?=null

    init {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
            createChannel()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        var channel=NotificationChannel(QAABEL_CHANNEL_ID,QAABEL_CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
        channel.lightColor=Color.BLUE
        channel.enableLights(true)
        channel.enableVibration(false)
        channel.lockscreenVisibility=Notification.VISIBILITY_PUBLIC
        getManager().createNotificationChannel(channel)



    }

     fun getManager():NotificationManager{
         if(notificationManager==null)
         notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager!!}

    @TargetApi(Build.VERSION_CODES.O)

     fun getQaabelNotification(title:String,
                                      message:String,
                                      sound:Uri,pendingIntent:PendingIntent): Notification.Builder {
        var largerIcon= BitmapFactory.decodeResource(applicationContext.resources,R.mipmap.ic_launcher)
        return Notification.Builder(applicationContext,QAABEL_CHANNEL_ID).
                setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .setLargeIcon(largerIcon)
                .setSound(sound)
                .setAutoCancel(true)
                   }

}