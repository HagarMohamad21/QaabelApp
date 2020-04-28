package com.Qaabel.org.viewModel.viewModel.account;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.Repository.Account.ResetPassRepository;


public class ResetPassViewModel extends AndroidViewModel
{
    private ResetPassRepository resetPassRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public ResetPassViewModel(@NonNull Application application)
    {
        super(application);
        resetPassRepository = new ResetPassRepository();
    }


    public LiveData<ApiLoginResponse> reset(String token, UserModel model)
    {
        return resetPassRepository.resetPass(token, model);
    }

    public LiveData<ApiLoginResponse> forgetPass(String token, UserModel model)
    {
        return resetPassRepository.ForgetPass(token, model);
    }
}