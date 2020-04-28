package com.Qaabel.org.viewModel.viewModel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.viewModel.Repository.Account.UploadImgRepository;

import okhttp3.MultipartBody;


public class UploadImgViewModel extends AndroidViewModel
{
    private UploadImgRepository uploadImgRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public UploadImgViewModel(@NonNull Application application)
    {
        super(application);
        uploadImgRepository = new UploadImgRepository();
    }


    public LiveData<ApiLoginResponse> uploadImage(MultipartBody.Part image)
    {
        return uploadImgRepository.UploadImg(image);
    }
}