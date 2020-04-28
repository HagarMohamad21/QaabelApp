package com.Qaabel.org.viewModel.Repository.Friend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.util.Log;

import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiNearPlacesResponse;
import com.Qaabel.org.model.Api.Response.ApiNearUsersResponse;

import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.entities.Active;
import com.Qaabel.org.model.entities.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearUserRepository
{
    public MutableLiveData<ApiNearUsersResponse> getNearFriend(String token)
    {
        final MutableLiveData<ApiNearUsersResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().getNearUsers(token).enqueue(new Callback<ApiNearUsersResponse>()
        {
            @Override
            public void onResponse(Call<ApiNearUsersResponse> call, Response<ApiNearUsersResponse> response)
            {

                if (response.body() != null)
                {
                    Log.d(TAG, "onResponse: ----------------888888-------------"+response.body().getUsers());
                    data.setValue(response.body());
                } else
                {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiNearUsersResponse> call, Throwable t)
            {
                Log.d(TAG, "onFailure: -----------------------------"+t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }

    private static final String TAG = "NearUserRepository";

    public MutableLiveData<ActiveResponse>isActive(String token , Active isActive){
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().active(token ,isActive).enqueue(new Callback<ActiveResponse>() {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response) {

                if(response.body()!=null){
                    data.setValue(response.body());

                }
                else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ----------------------"+t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<ApiNearPlacesResponse> getNearPlaces(String token, Location location) {
        UserModel userLocation=new UserModel(location);
        final MutableLiveData<ApiNearPlacesResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().getNearPlaces(token,userLocation).enqueue(new Callback<ApiNearPlacesResponse>() {
            @Override
            public void onResponse(Call<ApiNearPlacesResponse> call, Response<ApiNearPlacesResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ApiNearPlacesResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
