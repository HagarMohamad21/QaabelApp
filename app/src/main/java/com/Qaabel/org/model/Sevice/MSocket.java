package com.Qaabel.org.model.Sevice;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.view.activity.MainActivity;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MSocket extends Service
{

    public static Socket mSocket;

    public static final String ACTION_START_FOREGROUND_SERVICE = "let us go client ";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "shut it down client";
    public static final Integer ServiceId = 2121;
    public static String CHANNEL_ID = "channel_client";
    public static String CHANNEL_NAME = "connected_client";

    public enum SocketMassageType
    {
        Flash, FlashBack, Friend, TimeOut,verfied
    }

    Location location;
    Intent dialogIntent;

    static CountDownTimer countDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        super.onStartCommand(intent, flags, startId);
        dialogIntent = new Intent(this, MainActivity.class);
        try
        {
            startSocket();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        if (intent != null)
        {
            String action = intent.getAction();
            switch (action)
            {
                case ACTION_START_FOREGROUND_SERVICE:
//                    try
//                    {
//                        startSocket();
//
//                    } catch (URISyntaxException e)
//                    {
//                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopService();
                    break;
                default:
                    stopService();
                    break;

            }
        }
        return START_STICKY;
    }

    // start service
    public void startSocket() throws URISyntaxException
    {

        startForeground(ServiceId, buildNotification());


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    initializeSocket();
                } catch (Exception e)
                {
                    handler.postDelayed(this, 2000);
                }
            }
        });


    }

    private void stopService()
    {
        stopForeground(true);
        try
        {
            mSocket.disconnect();
        } catch (Exception e)
        {

        }
        stopSelf();
    }



    private void initializeSocket()
    {

        IO.Options opts = new IO.Options();
        opts.timeout = 10000;
        opts.forceNew = true;

        try
        {
            mSocket = IO.socket("http://Qaabel-env.rm7h6pvumf.us-east-1.elasticbeanstalk.com");

        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        // Create Connection
        mSocket.connect();
        Log.e("socket","Connected");
        setupEmitterListener();
    }

    private void setupEmitterListener()
    {
        String s=new SharedPref(getApplicationContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        Handler handler2 = new Handler();
        handler2.postDelayed(() ->
               mSocket.emit("register", s), 5000);
        Log.e("socket","register");

//        JSONObject jsonObject = new JSONObject();
//        try
//        {
//            jsonObject.put("token", new SharedPref(getApplicationContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN));
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }


        mSocket.on("flash", args -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                doAfterRequestConfirmed(SocketMassageType.Flash, (JSONObject) args[0]);
            });

        });
        mSocket.on("flashBack", args -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                doAfterRequestConfirmed(SocketMassageType.FlashBack, (JSONObject) args[0]);
            });

        });


    }

    private void doAfterRequestConfirmed(final SocketMassageType SocketMassageType, final JSONObject jsonObject)
    {
        final OnSocketListener on = MainActivity.onSocketListener;
        on.doAfterReceiveSocketData(SocketMassageType,jsonObject);
        {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                //call function
                {
                    final OnSocketListener on1 = MainActivity.onSocketListener;
                    on1.doAfterReceiveSocketData(SocketMassageType, jsonObject);
                    return;
                }

            }, 2000);
        }
    }

    private Notification buildNotification()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_logo).setContentTitle("Ailbas Client").setContentText("welcome from ailbaz").setWhen(new Date().getTime()).setPriority(NotificationCompat.PRIORITY_HIGH);
        return builder.build();

    }

}
