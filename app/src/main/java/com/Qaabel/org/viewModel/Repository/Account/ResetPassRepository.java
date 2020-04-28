package com.Qaabel.org.viewModel.Repository.Account;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassRepository
{
    private static final String TAG = "ResetPassRepository";
    public MutableLiveData<ApiLoginResponse> resetPass(String token,UserModel user)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().resetPass(token,user).enqueue(new Callback<ApiLoginResponse>()
        {
            
            @Override
            public void onResponse(Call<ApiLoginResponse> call, Response<ApiLoginResponse> response)
            {
                Log.d(TAG, "onResponse: 9999999999999999999999999999999999999999999"+response);

                if (response.body() != null)
                {
                    Log.d(TAG, "onResponse: ************************************ "+response.body());
                    data.setValue(response.body());
                } else
                {

                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiLoginResponse> call, Throwable t)
            {
                Log.d(TAG, "onFailure: ************************************ "+t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    } public MutableLiveData<ApiLoginResponse> ForgetPass(String token,UserModel user)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().forgotpassword(user).enqueue(new Callback<ApiLoginResponse>()
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
