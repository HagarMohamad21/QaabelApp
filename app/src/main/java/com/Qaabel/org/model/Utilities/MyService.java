package com.Qaabel.org.model.Utilities;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.Qaabel.org.model.Sevice.MSocket;
import com.Qaabel.org.model.Sevice.OnSocketListener;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.view.activity.MainActivity;


import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

 public class MyService
{
//    public static Socket mSocket;
//
//    public enum SocketMassageType
//    {
//        Flash, FlashBack, Friend, TimeOut
//    }
//
//
//    private static final String TAG = "MyLocationFRED";
//    private LocationManager mLocationManager = null;
//    private static final int LOCATION_INTERVAL = 1000;
//    private static final float LOCATION_DISTANCE = 10f;
//    Utilities utilities;
//
//    private class LocationListener implements android.location.LocationListener
//    {
//        Location mLastLocation;
//
//        public LocationListener(String provider)
//        {
//            Log.e(TAG, "LocationListener " + provider);
//            mLastLocation = new Location(provider);
//        }
//
//        @Override
//        public void onLocationChanged(Location location)
//        {
//            Log.e(TAG, "onLocationChanged: " + location);
////            location.setLatitude(0.0);
////            location.setLongitude(0.0);
//            //mLastLocation.set(location);
//           // utilities.SendMyLocation(location);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider)
//        {
//            Log.e(TAG, "onProviderDisabled: " + provider);
//        }
//
//        @Override
//        public void onProviderEnabled(String provider)
//        {
//            Log.e(TAG, "onProviderEnabled: " + provider);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras)
//        {
//            Log.e(TAG, "onStatusChanged: " + provider);
//        }
//    }
//
//    LocationListener[] mLocationListeners = new LocationListener[]{new LocationListener(LocationManager.GPS_PROVIDER), new LocationListener(LocationManager.NETWORK_PROVIDER)};
//
//    @Override
//    public IBinder onBind(Intent arg0)
//    {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        Log.e(TAG, "onStartCommand");
//        super.onStartCommand(intent, flags, startId);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable()
//        {
//
//            @Override
//            public void run()
//            {
//                try
//                {
//                    startSocket();
//                } catch (URISyntaxException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }, 3000);
//
//        // If we get killed, after returning from here, restart
//        return START_STICKY;
//
//    }
//
//    @Override
//    public void onCreate()
//    {
//        Log.e(TAG, "onCreate");
//        initializeLocationManager();
//        utilities = new Utilities(getApplicationContext());
//        try
//        {
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
//        } catch (SecurityException ex)
//        {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex)
//        {
//            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//        }
//        try
//        {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
//        } catch (SecurityException ex)
//        {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex)
//        {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }
//    }
//
//    @Override
//    public void onDestroy()
//    {
//        Log.e(TAG, "onDestroy");
//        super.onDestroy();
//        if (mLocationManager != null)
//        {
//            for (int i = 0; i < mLocationListeners.length; i++)
//            {
//                try
//                {
//                    mLocationManager.removeUpdates(mLocationListeners[i]);
//                } catch (Exception ex)
//                {
//                    Log.i(TAG, "fail to remove location listners, ignore", ex);
//                }
//            }
//        }
//    }
//
//    private void initializeLocationManager()
//    {
//        Log.e(TAG, "initializeLocationManager");
//        if (mLocationManager == null)
//        {
//            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        }
//    }
//
//
////    ?Socket
//
//    public void startSocket() throws URISyntaxException
//    {
//        checkPermission();
//        final Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//                    checkPermission();
//                    initializeSocket();
//                } catch (Exception e)
//                {
//                    handler.postDelayed(this, 2000);
//                }
//            }
//        });
//
//    }
//
//    private void initializeSocket()
//    {
//
//        IO.Options opts = new IO.Options();
//        opts.timeout = 10000;
//        opts.forceNew = true;
//
//        try
//        {
//            mSocket = IO.socket("http://Qaabel-env.rm7h6pvumf.us-east-1.elasticbeanstalk.com");
//
//        } catch (URISyntaxException e)
//        {
//            e.printStackTrace();
//        }
//
//        // Create Connection
//        mSocket.connect();
//        Log.e("socket", "Connected");
//        setupEmitterListener();
//    }
//
//    private void setupEmitterListener()
//    {
//        String s = new SharedPref(getApplicationContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
////        String s = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlMjJmN2IxM2I3NTNhOGU3NGNmYzEzMyIsImtleSI6IjlneGc4OCIsImlhdCI6MTU4MDY4MDE3N30.XCTrkxMMwhie-ab5k0q6p7raMe6CyaX2VZ2fUFFOgOQ";
//        Handler handler2 = new Handler();
//        handler2.postDelayed(() -> mSocket.emit("register", s), 5000);
//        Log.e("socket", "register");
//
//        mSocket.on("flash", args -> {
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(() -> {
//                doAfterRequestConfirmed(MSocket.SocketMassageType.Flash, (JSONObject) args[0]);
//            });
//        });
//        mSocket.on("flashBack", args -> {
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(() -> {
//                doAfterRequestConfirmed(MSocket.SocketMassageType.FlashBack, (JSONObject) args[0]);
//            });
//
//        });
//        mSocket.on("emailverified", args -> {
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(() -> {
////                Toast.makeText(this, "Email Verfied", Toast.LENGTH_SHORT).show();
//                doAfterRequestConfirmed(MSocket.SocketMassageType.verfied, (JSONObject) args[0]);
//            });
//
//        });
//    }
//
//    private void doAfterRequestConfirmed(final MSocket.SocketMassageType SocketMassageType, final JSONObject jsonObject)
//    {
//        final OnSocketListener on = MainActivity.onSocketListener;
//        on.doAfterReceiveSocketData(SocketMassageType, jsonObject);
//        {
//            if (SocketMassageType.equals(MSocket.SocketMassageType.verfied))
//            {
//                new SharedPref(getApplication()).saveString(AppSharedPrefs.SHARED_PREF_CONFIRM_EMAIL, "1");
//            }
//            final Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(() -> {
//                //call function
//                {
//                    final OnSocketListener on1 = MainActivity.onSocketListener;
//                    on1.doAfterReceiveSocketData(SocketMassageType, jsonObject);
//                    return;
//                }
//
//            }, 2000);
//        }
//    }
//
//    public void checkPermission()
//    {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//    }
}
