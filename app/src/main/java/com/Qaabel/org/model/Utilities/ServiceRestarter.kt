package com.Qaabel.org.model.Utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.Qaabel.org.helpers.isMyServiceRunning

class ServiceRestarter :BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        //restart service

              if(context!=null)
            if(!context.isMyServiceRunning(NotificationService::class.java))
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    context.startForegroundService(Intent(context,NotificationService::class.java))
                }
                else {
                    context.startService(Intent(context,NotificationService::class.java))
                }
        }


}