package com.Qaabel.org.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;

public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = "SplashActivity";
    String token;
    FriendProfileViewModel friendProfileViewModel;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        friendProfileViewModel= ViewModelProviders.of(this).get(FriendProfileViewModel.class);
       checkTokenValidity();

    }

    private void checkTokenValidity() {
        //Unauthorized
        token=new SharedPref(getBaseContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        phone=new SharedPref(getBaseContext()).getStrin(AppSharedPrefs.PHONE_VERIFIED);
        if(!token.equals("0")){

            //it means that user is logged in
            //now try to see if token is valid
            getUser();
        }
     else if (token.equals("0")||!phone.equals("TRUE"))
        {
            startActivity(new Intent(SplashActivity.this, AccountActivity.class));
            finish();
        }


    }


    private void getUser() {
        friendProfileViewModel.getUserProfile(token,new SharedPref(getApplicationContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)).observe(this,apiLoginResponse ->
        {
            if(apiLoginResponse!=null){
                if(apiLoginResponse.getMessage()!=null&&apiLoginResponse.getMessage().equals("Unauthorized")){
                 //show dialog
                    Log.d(TAG, "getUser: -------------------Unauthorized------------------------------");
                    Utilities.LogOut(this,true);
                }

                else{
                    startACtivity();
                }
            }
        });
    }


    public void startACtivity()
    {
        Log.d(TAG, "run: +++++++++++++++++++++++++++++++++++++++++++++++++++ TOKEN IS :::: "+token);
        Log.d(TAG, "run: +++++++++++++++++++++++++++++++++++++++++++++++++++ PHONE IS :::: "+phone);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();

    }


}
