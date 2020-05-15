package com.Qaabel.org.model.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.Qaabel.org.R;
import com.Qaabel.org.interfaces.OnBlockClicked;
import com.Qaabel.org.interfaces.OnLocationSent;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.NotificationModel;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.activity.AccountActivity;
import com.Qaabel.org.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.Qaabel.org.model.Sevice.MSocket.CHANNEL_NAME;


public class Utilities
{


    public static String Content_Type = "application/json";
    public static String COUNTRY = "COUNTRY";
    public static UserModel MsignUpUser = new UserModel();
    public static String Token = "0";
    public static MultipartBody.Part UploadedImg = null;
    ProgressDialog dialog;
    private static final String TAG = "Utilities";
    Context context;
    public OnLocationSent onLocationSent;
    private OnBlockClicked onBlockClicked=null;

    public void setOnLocationSent(OnLocationSent onLocationSent) {
        this.onLocationSent = onLocationSent;
    }

    public Utilities(Context context)
    {
        this.context = context;
        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        dialog.setMessage("please wait ...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }

    public Utilities()
    {
    }


    public static String BASE_URL = "http://Qaabel-env.rm7h6pvumf.us-east-1.elasticbeanstalk.com/";

    public void showDialog()
    {
        try
        {
            if (dialog != null) dialog.show();
        } catch (Exception e)
        {

        }
    }

    public void dismissDialog()
    {
        try
        {
            if (dialog != null) dialog.dismiss();

        } catch (Exception e)
        {
        }
    }

    public void SendMyLocation(Location location,Context context)
    {

            String token = new SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
            Utilities.MsignUpUser = new UserModel(location);

            Service.Fetcher.getInstance().sendMyLocation(Utilities.Content_Type, token, Utilities.MsignUpUser).enqueue(new Callback<ApiLoginResponse>()
            {
                @Override
                public void onResponse(Call<ApiLoginResponse> call, Response<ApiLoginResponse> response)
                {
                    if(response.body()!=null){
                        if(onLocationSent!=null)
                            onLocationSent.onLocationSent(response.body().getMessage());
                        else{
                            Log.d(TAG, "onResponse: ----------------------------------------ON LOCATION SENT IS NULL");
                        }
                        Log.d(TAG, "onResponse: --------------------------------------"+response.body().getMessage());
                    }
                    }



                @Override
                public void onFailure(Call<ApiLoginResponse> call, Throwable t)
                {
                    if(onLocationSent!=null)
                        onLocationSent.onLocationSent(t.getMessage());

                }
            });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getAge(String d)
    {
        Date mDate1 = getDate(d);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.setTime(mDate1);
        dob.set(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), mDate1.getDay());

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
        {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private static Date getDate(String d)
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
        try
        {
            date = format.parse(d);
            System.out.println(date);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return date;
    }


    public static void LogOut(Activity activity,boolean unvalidToken)
    {
        final Dialog dialog;
        dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_edit);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView yes = dialog.findViewById(R.id.yes_btn);
        TextView no = dialog.findViewById(R.id.no_btn);
        TextView blockBtn=dialog.findViewById(R.id.blockBtn);
        TextView cancelBtn=dialog.findViewById(R.id.cancelBtn);
        yes.setVisibility(View.VISIBLE);
         no.setVisibility(View.VISIBLE);
         blockBtn.setVisibility(View.GONE);
         cancelBtn.setVisibility(View.GONE);
        TextView msg = dialog.findViewById(R.id.msg);
         msg.setText("Are you sure you want to Log Out?");
         if (unvalidToken){
             msg.setText("You are logged out because you have logged in from another device");
             yes.setText("Log in");
         }

        yes.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.finish();
            new SharedPref(activity).saveString(AppSharedPrefs.SHARED_PREF_TOKRN, "0");
            new SharedPref(activity).saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER,null);
            new SharedPref(activity).setEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY,false);
            new SharedPref(activity).saveString(activity.getString(R.string.emailRegistered),"0");
            new SharedPref(activity).saveUser(AppSharedPrefs.TEMP_USER_FROM_COMPLETE_DATA,null);
            //MAKE USER OFFLINE
            activity.startActivity(intent);
            dialog.dismiss();


        });
        no.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    public  void generalUseDialog(Activity activity, String name, boolean unblock, boolean delete)
    {
        final Dialog dialog;
        dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_edit);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView yes = dialog.findViewById(R.id.yes_btn);
        TextView no = dialog.findViewById(R.id.no_btn);
        TextView blockBtn=dialog.findViewById(R.id.blockBtn);
        TextView cancelBtn=dialog.findViewById(R.id.cancelBtn);
        yes.setVisibility(View.GONE);
        no.setVisibility(View.GONE);
        blockBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        if(delete){
            blockBtn.setText("Delete");
        }
        if(unblock){
            blockBtn.setBackgroundResource(R.drawable.blue_btn);
            blockBtn.setText("Unblock");
        }

        blockBtn.setOnClickListener(v -> {


           if(onBlockClicked!=null){
               if(unblock){
                   onBlockClicked.onUnBlockClicked();
               }
               else
               onBlockClicked.onBlockClicked();

           }
            dialog.dismiss();


        });
        cancelBtn.setOnClickListener(view -> dialog.dismiss());
        TextView msg = dialog.findViewById(R.id.msg);
        if(unblock)
            msg.setText("Are you sure you want to Unblock "+name+" ?");
        else if(delete){
            msg.setText("Are you sure you want to delete these chats?");
        }
            else
            msg.setText("Are you sure you want to Block "+name+" ?");
        dialog.show();
    }


    public  void setOnBlockClicked(OnBlockClicked onBlockClicked) {
        this.onBlockClicked=onBlockClicked;
    }
}



