package com.Qaabel.org.model.Utilities

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
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

    override fun onBind(intent: Intent?): IBinder? {
          return null    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: -------------------SERVICE IS CREATED--------------------")
        mSocket= IO.socket(Utilities.BASE_URL)
        mtoken = SharedPref(applicationContext).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
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

        }

        catch (e: Exception) {
        }
    }




}