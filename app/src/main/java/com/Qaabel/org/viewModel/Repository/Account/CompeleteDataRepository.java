package com.Qaabel.org.viewModel.Repository.Account;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.entities.UserModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompeleteDataRepository
{
    private static final String TAG = "CompeleteDataRepository";
    public MutableLiveData<ApiLoginResponse> CompeleteData(String token, UserModel user)
    {

        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().CompeletData( token, user).enqueue(new Callback<ApiLoginResponse>()
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

    public LiveData<ApiLoginResponse> UploadImg(String token,MultipartBody.Part image)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().uploadProfilePhoto(token,image).enqueue(new Callback<ApiLoginResponse>()
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
                Log.d(TAG, "onFailure:********************************************************************** "+t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }
}
