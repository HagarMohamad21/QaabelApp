package com.Qaabel.org.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.SocketMessageModel;
import com.Qaabel.org.model.Sevice.MSocket;
import com.Qaabel.org.model.Sevice.OnSocketListener;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.MyService;
import com.Qaabel.org.model.Utilities.NotificationService;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.NotificationModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity
{
    private static final String CHANNEL_DESC = "noti";
    public BottomNavigationView bottomNavigationView;
    public static OnSocketListener onSocketListener;
    public static Socket mSocket;
    private CardView notificationCardView;
    private TextView name, event, action_one, action_two;
    private CircleImageView notify_img;
    FriendProfileViewModel profileViewModel;
    String token;
    NavHostFragment navHostFragment;

    public static OnSocketListener socketListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        startService(intent);

        bottomNavigationView = findViewById(R.id.bottom_nav);
         navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.shopping_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
        init();


    }

    private void init()
    {
        notificationCardView = findViewById(R.id.notification_card);
        name = findViewById(R.id.notify_name);
        event = findViewById(R.id.notify_event);
        action_one = findViewById(R.id.notify_action_one);
        action_two = findViewById(R.id.notify_action_two);
        notify_img = findViewById(R.id.notify_img);
        token = new SharedPref(this).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        profileViewModel = ViewModelProviders.of(this).get(FriendProfileViewModel.class);

        if(getIntent().getStringExtra("Notification")!=null&&getIntent().getStringExtra("Notification").equals("TRUE")){
         navHostFragment.getNavController().navigate(R.id.navigation_flash);
        }


    }
    private void initSocketActions()
    {
        socketListener=new OnSocketListener()
        {
            @Override
            public void doAfterReceiveSocketData(MSocket.SocketMassageType socketMassageType, JSONObject jsonObject)
            {
                if (socketMassageType.equals((MSocket.SocketMassageType.verfied))) {
                    recreate();}
            }

            @Override
            public void notificationRecieved(JSONObject jsonObject)
            {

            }

            @Override
            public void chatMessageRecieved(JSONObject jsonObject)
            {

            }
        };
    }
    private void startSocket()
    {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
//                    checkPermission();
                  //  initializeSocket();
                } catch (Exception e)
                {
                    handler.postDelayed(this, 2000);
                }
            }
        });
    }

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
//
//        } catch (URISyntaxException e)
//        {
//            e.printStackTrace();
//        }
//
//        // Create Connection
//        mSocket.connect();
//        setupEmitterListener();
//    }

    private void setupEmitterListener()
    {
        String token = new SharedPref(getApplicationContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        Handler handler2 = new Handler();
        handler2.postDelayed(() -> mSocket.emit("register", token), 5000);

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("token", new SharedPref(getApplicationContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        mSocket.on("flash", args -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                JSONObject jsonObject1 = (JSONObject) args[0];
//                NotificationModel notification = new NotificationModel("flashes", new FriendModel(jsonObject1));
//                Utilities.sendNotification(MainActivity.this, notification);
//                sendNotificationTest(notification);
//                doAfterRequestConfirmed(MSocket.SocketMassageType.Flash, (JSONObject) args[0]);
            });

        });
        mSocket.on("messag", args -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                JSONObject json = (JSONObject) args[0];
                SocketMessageModel socketMassageType=new SocketMessageModel(json);
                Toast.makeText(this, "dsjgdsjg", Toast.LENGTH_SHORT).show();
//                doAfterRequestConfirmed(MSocket.SocketMassageType.Flash, (JSONObject) args[0]);
            });

        });
        mSocket.on("flashBack", args -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                JSONObject jsonObject1 = (JSONObject) args[0];
//                NotificationModel notification = new NotificationModel("flash Back", new FriendModel(jsonObject1));
//                Utilities.sendNotification(MainActivity.this, notification);
//                sendNotificationTest(notification);
            });

        });


    }



    public void sendNotificationTest(NotificationModel notify)
    {
        notificationCardView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);
        notificationCardView.startAnimation(animation);
        name.setText(notify.getFriend().getName());
        event.setText(notify.getType() + " you");
        Picasso.get().load(notify.getFriend().getImage()).into(notify_img);
        final Handler handler = new Handler();


        if (notify.getType().equals("flashes"))
        {
            action_one.setText(R.string.view_profile);
            action_two.setText(R.string.flas_back);
            handler.postDelayed(() -> notificationCardView.setVisibility(View.GONE), 5000);
            //action_two.setOnClickListener(view -> profileViewModel.flashBackUser(token, notify.getFriend().getUser_id()));
        } else
        {
            action_one.setText(R.string.view_profile);
            action_two.setText(R.string.message);
            handler.postDelayed(() ->
                    Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show(), 5000);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

    }





}
