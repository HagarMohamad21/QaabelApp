package com.Qaabel.org.model.Utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.Qaabel.org.helpers.Notification
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.entities.UserModel
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


public class NotificationService : Service() {

    private var mSocket: Socket?=null
    private var mtoken=""
    private val TAG = "NotificationService"
    var broadCastIntent:Intent = Intent()

    init {
        broadCastIntent.action = "restartservice"
    }
    override fun onBind(intent: Intent?): IBinder? {
          return null    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: -------------------SERVICE IS CREATED--------------------")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground()
        else
            startForeground(1, android.app.Notification())
        mSocket= IO.socket(Utilities.BASE_URL)
        broadCastIntent.setClass(this,ServiceRestarter::class.java)
        mtoken = SharedPref(applicationContext).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = android.app.Notification.VISIBILITY_PRIVATE
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: android.app.Notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(android.app.Notification.CATEGORY_SERVICE)
                .build()
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //start socket
        startSocket()
        Log.d(TAG, "onCreate: -------------------SERVICE IS STARTED--------------------")

        return START_STICKY }

    private fun startSocket() {
        try {
            Log.d(TAG, "onCreate: -------------------SOCKET  IS CREATED--------------------")

            mSocket= IO.socket(Utilities.BASE_URL)
            mSocket?.let {
                it.connect()
                mSocket?.on(Socket.EVENT_CONNECT){
                    mSocket?.emit("register",mtoken)
                }
            }
            mSocket?.on(Socket.EVENT_DISCONNECT){
            }


            mSocket?.on("flash", Emitter.Listener {
              //show notification
                //Toast.makeText(applicationContext,"New Flash",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onCreate: -------------------NEW FLASH--------------------")

                var gson=Gson()
                var flashUser:UserModel= gson.fromJson(it[0].toString(),UserModel::class.java)
                var notification=Notification(applicationContext,"New Flash","${flashUser.name} just flashed you!")
                notification.sendNotification()
            })

            mSocket?.on("flashBack", Emitter.Listener {
              //show notification
                //Toast.makeText(applicationContext,"New Flash",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onCreate: -------------------NEW FLASH--------------------")

                var gson=Gson()
                var flashUser:UserModel= gson.fromJson(it[0].toString(),UserModel::class.java)
                var notification=Notification(applicationContext,"New Flash","${flashUser.name} just flashed you back!")
                notification.sendNotification()
            })



        }

        catch (e: Exception) {
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Toast.makeText(this, "ON TASK REMOVED", Toast.LENGTH_SHORT).show()

        this.sendBroadcast(broadCastIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "SERVICE ON DESTROY", Toast.LENGTH_SHORT).show()
       this.sendBroadcast(broadCastIntent)

    }


}