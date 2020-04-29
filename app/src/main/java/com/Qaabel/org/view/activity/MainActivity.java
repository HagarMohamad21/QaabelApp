package com.Qaabel.org.view.activity;

import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.Qaabel.org.R;
import com.Qaabel.org.model.Sevice.OnSocketListener;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.NotificationService;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.Qaabel.org.helpers.Common;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity
{
    private static final String CHANNEL_DESC = "noti";
    public BottomNavigationView bottomNavigationView;
    public static OnSocketListener onSocketListener;
    public static Socket mSocket;
    FriendProfileViewModel profileViewModel;
    String token;
    NavHostFragment navHostFragment;
    Intent serviceIntent;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate: ------------------------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(getApplicationContext(), NotificationService.class);
       if(!isMyServiceRunning(NotificationService.class)){
           Log.d(TAG, "onCreate: ----------------SERVICE IS RUNNING------------------");
           startService(serviceIntent);
       }

        bottomNavigationView = findViewById(R.id.bottom_nav);
         navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.shopping_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
        init();


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;

            }
        }
        return false;
    }

    private void init()
    {
        token = new SharedPref(this).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        profileViewModel = ViewModelProviders.of(this).get(FriendProfileViewModel.class);

        if(getIntent().getStringExtra("Notification")!=null&&getIntent().getStringExtra("Notification").equals("TRUE")){
            if(getIntent().getStringExtra("NotificationType").equals(Common.Companion.getNotificationType_FLASH()))
         navHostFragment.getNavController().navigate(R.id.navigation_flash);
            else if(getIntent().getStringExtra("NotificationType").equals(Common.Companion.getNotificationType_MESSAGE())){
                FriendModel nearUser=getIntent().getParcelableExtra("FRIEND_USER");
                Bundle bundle= new Bundle();
                bundle.putParcelable("FRIEND_USER",nearUser);
                navHostFragment.getNavController().navigate(R.id.navigation_chat,bundle);
            }
        }


    }





    @Override
    protected void onDestroy() {
       stopService(serviceIntent);
        super.onDestroy();

    }

}
