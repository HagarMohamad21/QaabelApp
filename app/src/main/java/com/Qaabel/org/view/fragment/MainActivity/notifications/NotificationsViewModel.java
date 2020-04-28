package com.Qaabel.org.view.fragment.MainActivity.notifications;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;


public class NotificationsViewModel extends AndroidViewModel
{

    private MutableLiveData<String> mText;

    public NotificationsViewModel(@NonNull Application application)
    {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }


    public LiveData<String> getText()
    {
        return mText;
    }
}