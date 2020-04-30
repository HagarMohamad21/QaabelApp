package com.Qaabel.org.viewModel.viewModel.friend;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiChatResponse;
import com.Qaabel.org.model.Api.Response.ApiFreindsResponse;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Response.MessageApiResponse;
import com.Qaabel.org.model.Api.Response.UserChatResponse;
import com.Qaabel.org.model.entities.ChatModel;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.MessageModel;
import com.Qaabel.org.model.entities.SentMessageModel;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.Repository.Friend.FriendProfileRepository;

import okhttp3.MultipartBody;


public class FriendProfileViewModel extends AndroidViewModel
{
    private FriendProfileRepository friendProfileRepository;
    private MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();


    public FriendProfileViewModel(@NonNull Application application)
    {
        super(application);
        friendProfileRepository = new FriendProfileRepository();
    }


    public LiveData<ApiLoginResponse> getUserProfile(String token, UserModel user)
    {
        return friendProfileRepository.getFriendProfile(token, user);

    }

    public LiveData<ApiLoginResponse> uploadImage(String token, MultipartBody.Part image)
    {
        return friendProfileRepository.UploadImg(token, image);

    }

    public LiveData<ApiLoginResponse> editProfile(String token, UserModel user)
    {
        return friendProfileRepository.editProfile(token, user);

    }

    public LiveData<ApiLoginResponse> blockUser(String token, String id)
    {
        return friendProfileRepository.BlockUser(token, id);
    }

    public LiveData<ActiveResponse> UnblockUser(String token, String id)
    {
        return friendProfileRepository.UnBlockUser(token, id);
    }

    public LiveData<ApiLoginResponse> flashUser(String token, String id)
    {
        return friendProfileRepository.FlashUser(token, id);
    }



//    public LiveData<ApiLoginResponse> deletAccount(String token)
//    {
//        return friendProfileRepository.deletAccount(token);
//    }

    public LiveData<ApiLoginResponse> unfriendUser(String token, String id)
    {
        return friendProfileRepository.unfriendUser(token, id);
    }

    public LiveData<ApiFreindsResponse> BlockList(String token)
    {
        return friendProfileRepository.getBlocks(token);
    }

    public LiveData<ApiFreindsResponse> getFlashes(String token)
    {
        return friendProfileRepository.getFlashes(token);
    }  public LiveData<ApiFreindsResponse> getBlocks(String token)
    {
        return friendProfileRepository.getBlocks(token);
    }

    public LiveData<ApiFreindsResponse> getFriends(String token)
    {
        return friendProfileRepository.getFriends(token);
    }

    public LiveData<ApiChatResponse> getChats(String token)
    {
        return friendProfileRepository.getChats(token);
    }
    public LiveData<MessageApiResponse> getMessages(String token, String chatId)
    {
        return friendProfileRepository.getMessages(token,chatId);
    }

    public MutableLiveData<MessageApiResponse> sendMessage(String token, SentMessageModel messageModel)
    {
        return friendProfileRepository.sendMessage(token,messageModel);

    }

    public MutableLiveData<ActiveResponse> deleteMessage(String token,String chatId){
        return friendProfileRepository.deleteMessage(token,chatId);
    }
    public LiveData<UserChatResponse> getUserChat(String token, String id)
    {
        return friendProfileRepository.getUserChat(token,id);

    }



}