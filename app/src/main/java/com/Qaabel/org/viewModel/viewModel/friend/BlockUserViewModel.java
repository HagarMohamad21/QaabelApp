package com.Qaabel.org.viewModel.viewModel.friend;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.viewModel.Repository.Friend.UserBlockRepository;


public class BlockUserViewModel extends AndroidViewModel
{
    private UserBlockRepository userBlockRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public BlockUserViewModel(@NonNull Application application)
    {
        super(application);
        userBlockRepository = new UserBlockRepository();
    }


    public LiveData<ApiLoginResponse> blockUser(String token, String id)
    {
        return userBlockRepository.BlockUser(token, id);
    }
}