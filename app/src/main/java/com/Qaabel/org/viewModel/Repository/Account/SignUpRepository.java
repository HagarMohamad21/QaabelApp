package com.Qaabel.org.viewModel.Repository.Account;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Response.ApiSignUpResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepository
{
    public MutableLiveData<ApiSignUpResponse> register(UserModel user)
    {
        final MutableLiveData<ApiSignUpResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().signUp(Utilities.Content_Type, user).enqueue(new Callback<ApiSignUpResponse>()
        {
            @Override
            public void onResponse(Call<ApiSignUpResponse> call, Response<ApiSignUpResponse> response)
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
            public void onFailure(Call<ApiSignUpResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<ApiLoginResponse> activePhone(String token,UserModel user)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().activePhone(token, user).enqueue(new Callback<ApiLoginResponse>()
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
    public MutableLiveData<ApiLoginResponse> resendCode(String token)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().resendCode(token).enqueue(new Callback<ApiLoginResponse>()
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


    public LiveData<ApiSignUpResponse> editSignupData(String token, UserModel userModel) {
        final MutableLiveData<ApiSignUpResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().EditSignupData(token,userModel).enqueue(new Callback<ApiSignUpResponse>() {
            @Override
            public void onResponse(Call<ApiSignUpResponse> call, Response<ApiSignUpResponse> response) {
                if (response.body() != null)
                {
                    data.setValue(response.body());
                } else
                {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiSignUpResponse> call, Throwable t) {
                data.setValue(null);

            }
        });

    return  data;}

    public LiveData<ApiSignUpResponse> changeEmail(String token, UserModel userModel) {
        final MutableLiveData<ApiSignUpResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().changeEmail(token,userModel).enqueue(new Callback<ApiSignUpResponse>() {
            @Override
            public void onResponse(Call<ApiSignUpResponse> call, Response<ApiSignUpResponse> response) {
                if (response.body() != null)
                {
                    data.setValue(response.body());
                } else
                {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiSignUpResponse> call, Throwable t) {
              data.setValue(null);
            }
        });
    return data;}
}
