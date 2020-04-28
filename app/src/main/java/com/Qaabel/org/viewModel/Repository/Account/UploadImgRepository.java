package com.Qaabel.org.viewModel.Repository.Account;

import android.arch.lifecycle.MutableLiveData;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.Utilities.Utilities;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImgRepository
{
    public MutableLiveData<ApiLoginResponse> UploadImg(MultipartBody.Part image)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().uploadProfilePhoto(Utilities.Content_Type,image).enqueue(new Callback<ApiLoginResponse>()
        {
            @Override
            public void onResponse(Call<ApiLoginResponse> call, Response<ApiLoginResponse> response)
            {
                if (response.body() != null)
                {
                    data.setValue(response.body());
                } else
                {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiLoginResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }


}
