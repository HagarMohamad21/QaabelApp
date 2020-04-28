package com.Qaabel.org.viewModel.Repository.Friend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiChatResponse;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.entities.FriendId;
import com.Qaabel.org.model.entities.FriendModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFlashRepository
{
    public MutableLiveData<ActiveResponse> FlashUser(String token,String id)
    {
        FriendId model = new FriendId();
        model.setUser_id(id);
         final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().flashUser( token, model).enqueue(new Callback<ActiveResponse>() {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response) {
                if (response.body()!=null)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t) {
              data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ActiveResponse> FlashUserBack(String token, String id) {

        FriendId model = new FriendId();
        model.setUser_id(id);
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().flashBackUser( token, model).enqueue(new Callback<ActiveResponse>() {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response) {
                if (response.body()!=null)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;

    }
    public LiveData<ActiveResponse> IgnoreUser(String token,String id){
        FriendId model = new FriendId();
        model.setUser_id(id);
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().ignoreUser(token,model).enqueue(new Callback<ActiveResponse>() {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response) {
                if(response.body()!=null){
                    data.setValue(response.body());
                }
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ActiveResponse> blockUser(String token, String id) {
        FriendId model = new FriendId();
        model.setUser_id(id);
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().blockUser(token,model).enqueue(new Callback<ActiveResponse>() {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response) {
                if(response.body()!=null){
                     data.setValue(response.body());
                }
                else data.setValue(null);
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t) {
                 data.setValue(null);

            }
        });
        return data;
    }
}
