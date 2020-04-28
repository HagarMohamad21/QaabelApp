package com.Qaabel.org.viewModel.Repository.Friend;

import android.arch.lifecycle.MutableLiveData;

import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.FriendModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBlockRepository
{
    public MutableLiveData<ApiLoginResponse> BlockUser(String token, String id)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();
//        FriendModel model = new FriendModel(id);
//
//        Service.Fetcher.getInstance().blockUser(Utilities.Content_Type, token, model).enqueue(new Callback<ApiLoginResponse>()
//        {
//            @Override
//            public void onResponse(Call<ApiLoginResponse> call, Response<ApiLoginResponse> response)
//            {
//                if (response.body() != null)
//                {
//                    data.setValue(response.body());
//                } else
//                {
//                    data.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiLoginResponse> call, Throwable t)
//            {
//                data.setValue(null);
//            }
//        });

        return data;
    }
}
