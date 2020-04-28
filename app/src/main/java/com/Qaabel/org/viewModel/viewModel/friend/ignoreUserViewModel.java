package com.Qaabel.org.viewModel.viewModel.friend;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.viewModel.Repository.Friend.UserIgnoreRepository;


public class ignoreUserViewModel extends AndroidViewModel
{
    private UserIgnoreRepository ignoreRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public ignoreUserViewModel(@NonNull Application application)
    {
        super(application);
        ignoreRepository = new UserIgnoreRepository();

    }


    public LiveData<ApiLoginResponse> ignoreUser(String token,String id)
    {
        return ignoreRepository.IgnoreUser(token,id);
    }
}