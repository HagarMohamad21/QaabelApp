package com.Qaabel.org.viewModel.viewModel.account;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;


import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.viewModel.Repository.Account.LoginRepository;


public class LoginViewModel extends AndroidViewModel
{
    private LoginRepository loginRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application)
    {
        super(application);
        loginRepository = new LoginRepository();
     }



    public LiveData<ApiLoginResponse> login()
    {
        return loginRepository.login(Utilities.MsignUpUser);
    }
}