package com.Qaabel.org.viewModel.viewModel.friend;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiChatResponse;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.viewModel.Repository.Friend.UserFlashRepository;


public class FlashUserViewModel extends AndroidViewModel
{
    private UserFlashRepository userFlashRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public FlashUserViewModel(@NonNull Application application)
    {
        super(application);
        userFlashRepository = new UserFlashRepository();

    }


    public LiveData<ActiveResponse> flashUser(String token, String id)
    {
        return userFlashRepository.FlashUser(token,id);
    }
    public LiveData<ActiveResponse> flashUserBack(String token, String id)
    {
        return userFlashRepository.FlashUserBack(token,id);
    }
    public LiveData<ActiveResponse> blockUser(String token,String id){
        return userFlashRepository.blockUser(token,id);
    }
    public LiveData<ActiveResponse> ignoreUser(String token,String id){
        return userFlashRepository.IgnoreUser(token,id);
    }

}