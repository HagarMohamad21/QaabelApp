package com.Qaabel.org.viewModel.viewModel.friend;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiNearPlacesResponse;
import com.Qaabel.org.model.Api.Response.ApiNearUsersResponse;
import com.Qaabel.org.model.Api.Response.UsersInPlaceResponse;
import com.Qaabel.org.model.entities.Active;
import com.Qaabel.org.viewModel.Repository.Friend.NearUserRepository;

import org.jetbrains.annotations.NotNull;


public class NearUsersViewModel extends AndroidViewModel
{
    private NearUserRepository nearUserRepository;
    private MutableLiveData<ApiNearUsersResponse> data = new MutableLiveData<>();


    public NearUsersViewModel(@NonNull Application application)
    {
        super(application);
        nearUserRepository = new NearUserRepository();

    }



    public LiveData<ApiNearUsersResponse> nearUsers(String token)
    {
        return nearUserRepository.getNearFriend(token);
    }

    public LiveData<ActiveResponse> isActive(String token , Active isActive){
        return nearUserRepository.isActive(token,isActive);
    }


    public LiveData<ApiNearPlacesResponse> nearPlaces(String token, Location location){
        return nearUserRepository.getNearPlaces(token,location);
    }

    public LiveData<UsersInPlaceResponse> usersInPlace(String token, Location location){
        return nearUserRepository.getUsersInPlace(token,location);
    }


}