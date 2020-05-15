package com.Qaabel.org.viewModel.Repository.Friend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiChatResponse;
import com.Qaabel.org.model.Api.Response.ApiFreindsResponse;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Response.MessageApiResponse;
import com.Qaabel.org.model.Api.Response.UserChatResponse;
import com.Qaabel.org.model.Api.Service;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.ChatModel;
import com.Qaabel.org.model.entities.ChatReadModel;
import com.Qaabel.org.model.entities.FriendId;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.MessageModel;
import com.Qaabel.org.model.entities.SentMessageModel;
import com.Qaabel.org.model.entities.UserModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FriendProfileRepository
{
    private static final String TAG = "FriendProfileRepository";
    public MutableLiveData<ApiLoginResponse> getFriendProfile(String token, UserModel user)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().getProfileInfo(token).enqueue(new Callback<ApiLoginResponse>()
        {
            @Override
            public void onResponse(Call<ApiLoginResponse> call, Response<ApiLoginResponse> response)
            {
                Log.d(TAG, "onResponse: ---------00000000000000000--------"+response.message());
                if(response.message().equals("Unauthorized")&&response.code()==401){
                    ApiLoginResponse apiLoginResponse=new ApiLoginResponse();
                    apiLoginResponse.setMessage("Unauthorized");
                    data.setValue(apiLoginResponse);
                }

                if (response.body() != null)
                {
                    Log.d(TAG, "onResponse: ------------------------------NOT NULL---------------------");
                    data.setValue(response.body());
                } else
                {

                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiLoginResponse> call, Throwable t)
            {
                Log.d(TAG, "onFailure: *************************************************"+t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<ApiLoginResponse> editProfile(String token, UserModel user)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();
//        user.setOldPassword("12345678");
        Service.Fetcher.getInstance().editProfile(token, user).enqueue(new Callback<ApiLoginResponse>()
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

    public LiveData<ApiLoginResponse> UploadImg(String token, MultipartBody.Part image)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().uploadProfilePhoto(token, image).enqueue(new Callback<ApiLoginResponse>()
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

    public MutableLiveData<ApiLoginResponse> IgnoreUser(String token, String id)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();
//        FriendModel model = new FriendModel(id);
//        Service.Fetcher.getInstance().ignoreUser(Utilities.Content_Type, token, model).enqueue(new Callback<ApiLoginResponse>()
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
//
//        return data;
//    } public MutableLiveData<ApiLoginResponse> deletAccount(String token)
//    {
//        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();
//         Service.Fetcher.getInstance().deletAccount(Utilities.Content_Type, token).enqueue(new Callback<ApiLoginResponse>()
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

    public MutableLiveData<ActiveResponse> UnBlockUser(String token, String id)
    {
        FriendId model = new FriendId();
        model.setUser_id(id);
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().UnblockUser(token,model).enqueue(new Callback<ActiveResponse>() {
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

    public MutableLiveData<ApiLoginResponse> unfriendUser(String token, String id)
    {
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

//        Service.Fetcher.getInstance().unfriendUser(Utilities.Content_Type, token, new FriendModel(id)).enqueue(new Callback<ApiLoginResponse>()
//        {
//            @Override
//            public void onResponse(Call<ApiLoginResponse> call, Response<ApiLoginResponse> response)
//            {
//                if (response.body() != null)
//                {
//
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

    public MutableLiveData<ApiLoginResponse> FlashUser(String token, String id)
    {
//        FriendModel model = new FriendModel(id);
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();
//
//        Service.Fetcher.getInstance().flashUser(token, model).enqueue(new Callback<ApiLoginResponse>()
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

    public MutableLiveData<ApiLoginResponse> FlashBackUser(String token, String id)
    {
        //FriendModel model = new FriendModel(id);
        final MutableLiveData<ApiLoginResponse> data = new MutableLiveData<>();

//        Service.Fetcher.getInstance().flashBackUser(token, model).enqueue(new Callback<ApiLoginResponse>()
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

    public MutableLiveData<ApiFreindsResponse> getFlashes(String token)
    {
        final MutableLiveData<ApiFreindsResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().getFlashes(token).enqueue(new Callback<ApiFreindsResponse>()
        {
            @Override
            public void onResponse(Call<ApiFreindsResponse> call, Response<ApiFreindsResponse> response)
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
            public void onFailure(Call<ApiFreindsResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<ApiFreindsResponse> getBlocks(String token)
    {
        final MutableLiveData<ApiFreindsResponse> data = new MutableLiveData<>();

        Service.Fetcher.getInstance().BlockList(token).enqueue(new Callback<ApiFreindsResponse>()
        {
            @Override
            public void onResponse(Call<ApiFreindsResponse> call, Response<ApiFreindsResponse> response)
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
            public void onFailure(Call<ApiFreindsResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<ApiFreindsResponse> getFriends(String token)
    {
//        token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlMjJmNmEzM2I3NTNhOGU3NGNmNWJlZSIsImtleSI6InRtNDVwMyIsImlhdCI6MTU3OTM0OTg2Nn0.nBfEHGBBuEH8BzPwFYT_moGlmmuSdN5qVNKig3wqK_g";
        final MutableLiveData<ApiFreindsResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().getFriends(token).enqueue(new Callback<ApiFreindsResponse>()
        {
            @Override
            public void onResponse(Call<ApiFreindsResponse> call, Response<ApiFreindsResponse> response)
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
            public void onFailure(Call<ApiFreindsResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }


    public MutableLiveData<ApiChatResponse> getChats(String token)
    {
        final MutableLiveData<ApiChatResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().getChats(token).enqueue(new Callback<ApiChatResponse>()
        {
            @Override
            public void onResponse(Call<ApiChatResponse> call, Response<ApiChatResponse> response)
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
            public void onFailure(Call<ApiChatResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<MessageApiResponse> getMessages(String token, String chatId)
    {
        ChatModel chatModel=new ChatModel();
        chatModel.setChat_id(chatId);
        chatModel.setPage("1");
        final MutableLiveData<MessageApiResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().getMessages(token,chatModel).enqueue(new Callback<MessageApiResponse>()
        {
            @Override
            public void onResponse(Call<MessageApiResponse> call, Response<MessageApiResponse> response)
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
            public void onFailure(Call<MessageApiResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<MessageApiResponse> sendMessage(String token, SentMessageModel messageModel)
   {
        final MutableLiveData<MessageApiResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().sendMessage(token,messageModel).enqueue(new Callback<MessageApiResponse>()
        {
            @Override
            public void onResponse(Call<MessageApiResponse> call, Response<MessageApiResponse> response)
            {
                Log.d(TAG, "onResponse: -----------------------"+response.body());
                if (response.body() != null)
                {

                    data.setValue(response.body());
                } else
                {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MessageApiResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<ActiveResponse> MakeRead(String token, ChatReadModel chatReadModel)
    {
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().makeRead(token,chatReadModel).enqueue(new Callback<ActiveResponse>()
        {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response)
            {
                Log.d(TAG, "onResponse: -----------------------"+response.body());
                if (response.body() != null)
                {

                    data.setValue(response.body());
                } else
                {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t)
            {
                data.setValue(null);
            }
        });

        return data;
    }




    public LiveData<UserChatResponse> getUserChat(String token, String userId){
         FriendId friendId=new FriendId();
         friendId.setUser_id(userId);

        final MutableLiveData<UserChatResponse> data = new MutableLiveData<>();
        Service.Fetcher.getInstance().getUserChat(token,friendId).enqueue(new Callback<UserChatResponse>() {
            @Override
            public void onResponse(Call<UserChatResponse> call, Response<UserChatResponse> response) {
                 data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserChatResponse> call, Throwable t) {
               data.setValue(null);
            }
        });
    return data;}

    public MutableLiveData<ActiveResponse> deleteMessage(String token, String chatId) {
        final MutableLiveData<ActiveResponse> data = new MutableLiveData<>();
        ChatModel chatModel=new ChatModel(); chatModel.setChat_id(chatId);
        Service.Fetcher.getInstance().deleteChat(token,chatModel).enqueue(new Callback<ActiveResponse>() {
            @Override
            public void onResponse(Call<ActiveResponse> call, Response<ActiveResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ActiveResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
