package com.Qaabel.org.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;

public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        checkLogin();
        startACtivity();
        Log.d(TAG, "onCreate: 888888888888888888888888888888888888888888888");
    }




    public void startACtivity()
    {
        Thread splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 200)
                    {
                        sleep(100);
                        waited += 100;
                    }

                } catch (InterruptedException e)
                {
                } finally
                {
                    String token=new SharedPref(getBaseContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
                    String phone=new SharedPref(getBaseContext()).getStrin(AppSharedPrefs.PHONE_VERIFIED);
                    Log.d(TAG, "run: +++++++++++++++++++++++++++++++++++++++++++++++++++ TOKEN IS :::: "+token);
                    Log.d(TAG, "run: +++++++++++++++++++++++++++++++++++++++++++++++++++ PHONE IS :::: "+phone);

                    if (token.equals("0")||!phone.equals("TRUE"))
                    {
                        startActivity(new Intent(SplashActivity.this, AccountActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }


                }
            }

        };
        splashTread.start();

    }


}
