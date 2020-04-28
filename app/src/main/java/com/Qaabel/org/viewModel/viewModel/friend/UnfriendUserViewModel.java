package com.Qaabel.org.viewModel.viewModel.friend;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.viewModel.Repository.Friend.UserUnfriendRepository;


public class UnfriendUserViewModel extends AndroidViewModel
{
    private UserUnfriendRepository userUnfriendRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public UnfriendUserViewModel(@NonNull Application application)
    {
        super(application);
        userUnfriendRepository = new UserUnfriendRepository();
        this.data = userUnfriendRepository.unfriendUser(Utilities.Token);
    }


    public LiveData<ApiLoginResponse> unfriendUser(String token)
    {
        return userUnfriendRepository.unfriendUser(token);
    }
}