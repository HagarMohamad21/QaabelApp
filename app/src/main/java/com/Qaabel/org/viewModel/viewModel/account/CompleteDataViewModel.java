package com.Qaabel.org.viewModel.viewModel.account;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.Repository.Account.CompeleteDataRepository;

import okhttp3.MultipartBody;


public class CompleteDataViewModel extends AndroidViewModel
{
    private CompeleteDataRepository compeleteDataRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public CompleteDataViewModel(@NonNull Application application)
    {
        super(application);
        compeleteDataRepository = new CompeleteDataRepository();
    }



    public LiveData<ApiLoginResponse> completeData(String token,UserModel userModel)
    {
        return  compeleteDataRepository.CompeleteData(token,userModel);

    }

    public LiveData<ApiLoginResponse> uploadImage(String token,MultipartBody.Part image)
    {
        return compeleteDataRepository.UploadImg(token,image);
    }
}