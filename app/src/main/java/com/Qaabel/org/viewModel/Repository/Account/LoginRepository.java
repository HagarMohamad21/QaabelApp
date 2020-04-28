package com.Qaabel.org.viewModel.Repository.Account;

import android.arch.lifecycle.MutableLiveData;



import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository
{
    public MutableLiveData<ApiLoginResponse> login(UserModel user)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().login(Utilities.Content_Type,user).enqueue(new Callback<ApiLoginResponse>()
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
