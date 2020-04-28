package com.Qaabel.org.viewModel.viewModel.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Response.ApiSignUpResponse;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.Repository.Account.SignUpRepository;

public class SignUpViewModel extends AndroidViewModel
{

    private SignUpRepository signUpRepository;


    public SignUpViewModel(@NonNull Application application)
    {
        super(application);
        signUpRepository = new SignUpRepository();
    }

    public LiveData<ApiSignUpResponse> register(UserModel user)
    {
        return signUpRepository.register(user);
    }
    public LiveData<ApiLoginResponse> activePhone(String token, UserModel user)
    {
        return signUpRepository.activePhone(token,user);
    }
    public LiveData<ApiLoginResponse> resendCode(String token)
    {
        return signUpRepository.resendCode(token);
    }
    public LiveData<ApiSignUpResponse> editSignupData(String token,UserModel userModel)
    {
        return  signUpRepository.editSignupData(token,userModel);

    }

    public LiveData<ApiSignUpResponse> changeEmail(String token,UserModel userModel)
    {
        return  signUpRepository.changeEmail(token,userModel);

    }




}
