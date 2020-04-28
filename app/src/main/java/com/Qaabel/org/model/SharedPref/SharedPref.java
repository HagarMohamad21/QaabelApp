package com.Qaabel.org.model.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.Qaabel.org.model.entities.NotificationModel;
import com.Qaabel.org.model.entities.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPref
{
    Context context;
    final String myPreferanceName = "AbshALomRef";
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public SharedPref(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(myPreferanceName, 0);
        editor = pref.edit();


    }

    public void setEmailVerified(String key,boolean isEmailVerified){
        editor.putBoolean(key,isEmailVerified);
        editor.commit();
    }

    public boolean isEmailVerified(String key){
        return pref.getBoolean(key,false);
    }

    public void saveString(String key, String user_id)
    {
        editor.putString(key, user_id); // Storing string
        editor.commit();
    }

    public void saveInt(String key, int value)
    {
        editor.putInt(key, value); // Storing int
        editor.commit();
    }

    public String getStrin(String Key)
    {
        String t = pref.getString(Key, "0");
        return (t);
    }  public int getInt(String Key)
    {
        int t = pref.getInt(Key, 0);
        return (t);
    }


    public void setMeals(String Key, List<UserModel> users)
    {
        Gson gson = new Gson();
        String json = gson.toJson(users);

        editor.putString(Key, json); // Storing string
        editor.commit();
    }

    public void saveUser(String Key, UserModel user)
    {

        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString(Key, json); // Storing string
        editor.commit();
    }


    public UserModel getUser(String Key)
    {
        UserModel user;
        Gson gson = new Gson();
        String json = pref.getString(Key, "");
        Type type = new TypeToken<UserModel>()
        {
        }.getType();
        user = gson.fromJson(json, type);
        return user;
    }


    public void saveNotifivation(String Key, NotificationModel notification)
    {
        List<NotificationModel> notifications = getNotifications(Key);
        if (notifications == null)
        {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        Gson gson = new Gson();
        String json = gson.toJson(notifications);

        editor.putString(Key, json); // Storing string
        editor.commit();
    }

    public List<NotificationModel> getNotifications(String key)
    {
        List<NotificationModel> meals = new ArrayList<>();
        Gson gson = new Gson();
        String json = pref.getString(key, "");
        Type type = new TypeToken<ArrayList<NotificationModel>>()
        {
        }.getType();
        meals = gson.fromJson(json, type);
        return meals;
    }
}