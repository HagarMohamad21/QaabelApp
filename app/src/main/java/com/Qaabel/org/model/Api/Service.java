package com.Qaabel.org.model.Api;


import com.Qaabel.org.model.Api.Response.ActiveResponse;
import com.Qaabel.org.model.Api.Response.ApiChatResponse;
import com.Qaabel.org.model.Api.Response.ApiFreindsResponse;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.Api.Response.ApiNearPlacesResponse;
import com.Qaabel.org.model.Api.Response.ApiNearUsersResponse;
import com.Qaabel.org.model.Api.Response.ApiSignUpResponse;
import com.Qaabel.org.model.Api.Response.MessageApiResponse;
import com.Qaabel.org.model.Api.Response.UserChatResponse;
import com.Qaabel.org.model.Api.Response.UsersInPlaceResponse;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.Active;
import com.Qaabel.org.model.entities.ChatModel;
import com.Qaabel.org.model.entities.FriendId;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.MessageModel;
import com.Qaabel.org.model.entities.SentMessageModel;
import com.Qaabel.org.model.entities.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Fred on 1/3/2019.
 */

public interface Service
{

    String BASE_URL = Utilities.BASE_URL;

    @Headers({"Accept: application/json"})
    @POST("user/signin")
    Call<ApiLoginResponse> login(@Header("Content-Type") String Accept, @Body UserModel user);


    @Headers({"Accept: application/json"})
    @POST("user")
    Call<ApiSignUpResponse> signUp(@Header("Content-Type") String Accept, @Body UserModel user);

    @Headers({"Accept: application/json"})
    @PUT("user/completedata")
    Call<ApiLoginResponse> CompeletData(@Header("authToken") String token, @Body UserModel user);

    @Headers({"Accept: application/json"})
    @PUT("user/changeSignupData")
    Call<ApiSignUpResponse> EditSignupData(@Header("authToken") String token, @Body UserModel user);

    @Headers({"Accept: application/json"})
    @POST("user/activephone")
    Call<ApiLoginResponse> activePhone(@Header("authToken") String token, @Body UserModel user);

    @Headers({"Accept: application/json"})
    @PUT("/user/active")
    Call<ActiveResponse> active(@Header("authToken") String token,@Body Active active);



    @Headers({"Accept: application/json"})
    @POST("user/resendphonecode")
    Call<ApiLoginResponse> resendCode(@Header("authToken") String token);

//    @Headers({"Accept: application/json"})
//    @POST("user/getnotifications")
//    Call<ApiLoginResponse> resendCode(@Header("authToken") String token);

    @Headers("Content-Type: application/json")
    @POST("user/resetpassword/:code")
    Call<ApiLoginResponse> resetPass(@Header("authToken") String token, @Body UserModel user);

    @Headers("Content-Type: application/json")
    @POST("user/forgotpassword")
    Call<ApiLoginResponse> forgotpassword(@Body UserModel user);


    @Headers({"Accept: application/json"})
    @PUT("user/location")
    Call<ApiLoginResponse> sendMyLocation(@Header("Content-Type") String Accept, @Header("authToken") String token, @Body UserModel user);



     @Headers({"Accept: application/json"})
    @GET("user/getnearusers")
    Call<ApiNearUsersResponse> getNearUsers(@Header("authToken") String token);



    @Headers("Content-Type: application/json")
    @POST("user/ignoreuser")
    Call<ActiveResponse> ignoreUser(@Header("authToken") String token, @Body FriendId userModel);

 @Headers({"Accept: application/json"})
    @DELETE("user/deleteaccount")
    Call<ApiLoginResponse> deletAccount(@Header("Content-Type") String Accept, @Header("authToken") String token);

    @Headers("Content-Type: application/json")
    @POST("user/flashuser")
    Call<ActiveResponse> flashUser(@Header("authToken") String token, @Body FriendId userModel);


    @Headers({"Accept: application/json"})
    @POST("user/block")
    Call<ActiveResponse> blockUser(@Header("authToken") String token, @Body FriendId userModel);

    @Headers({"Accept: application/json"})
    @POST("user/unblock")
    Call<ActiveResponse> UnblockUser(@Header("authToken") String token, @Body FriendId userModel);


    @Headers("Content-Type: application/json")
    @POST("user/flashbackuser")
    Call<ActiveResponse> flashBackUser(@Header("authToken") String token, @Body FriendId friendId);

    @Headers("Type: application/json")
    @GET("user/getblocks")
    Call<ApiFreindsResponse> BlockList(@Header("authToken") String token);

    @Multipart
    @POST("user/image")
    Call<ApiLoginResponse> uploadProfilePhoto(@Header("authToken") String token, @Part MultipartBody.Part photo);

    @Headers({"Accept: application/json"})
    @PUT("user/editprofile")
    Call<ApiLoginResponse> editProfile(@Header("authToken") String token, @Body UserModel user);

    @Headers({"Accept: application/json"})
    @GET("user/getfriends")
    Call<ApiFreindsResponse> getFriends(@Header("authToken") String token);

    @Headers({"Accept: application/json"})
    @GET("user/getflashes")
    Call<ApiFreindsResponse> getFlashes(@Header("authToken") String token);

    @Headers("Content-Type: application/json")
    @GET("user/getchats")
    Call<ApiChatResponse> getChats(@Header("authToken") String token);

    @Headers("Content-Type: application/json")
    @POST("user/getuserchat")
    Call<UserChatResponse> getUserChat(@Header("authToken") String token, @Body  FriendId friendUser);

    @Headers("Content-Type: application/json")
    @POST("chat/getmessages")
    Call<MessageApiResponse> getMessages(@Header("authToken") String token, @Body ChatModel chatModel);

    @Headers("Content-Type: application/json")
    @POST("chat/deletechat")
    Call<ActiveResponse> deleteChat(@Header("authToken") String token, @Body ChatModel chatModel);

 @Headers("Content-Type: application/json")
    @POST("chat/sendmessage")
    Call<MessageApiResponse> sendMessage(@Header("authToken") String token, @Body SentMessageModel chatModel);

    @Headers({"Accept: application/json"})
    @POST("user/changeemail")
    Call<ApiSignUpResponse> changeEmail(@Header("authToken") String token, @Body UserModel userModel);


    @Headers({"Accept: application/json"})
    @GET("/user/getprofileinfo")
    Call<ApiLoginResponse> getProfileInfo(@Header("authToken") String token);


     @Headers({"Accept: application/json"})
    @POST("/user/getnearplaces")
    Call<ApiNearPlacesResponse> getNearPlaces(@Header("authToken") String token,@Body UserModel userModel);

 @Headers({"Accept: application/json"})
    @POST("/user/getusersinplace")
    Call<UsersInPlaceResponse> getUsersInPlace(@Header("authToken") String token, @Body UserModel userModel);




    class Fetcher
    {
        private static Service service;


        public static Service getInstance()
        {
            if (service == null)
            {
                Interceptor interceptor = chain -> {
                    Request newRequest = chain.request().newBuilder().build();

                    return chain.proceed(newRequest);
                };

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                Dispatcher dispatcher = new Dispatcher();
                dispatcher.setMaxRequests(1);
                builder.interceptors().add(interceptor);
                builder.dispatcher(dispatcher).connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).cache(null);
                OkHttpClient client = builder.build();
                OkHttpClient client2 = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS).build();

                Gson gson = new GsonBuilder().setLenient().create();

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(client2).build();

                service = retrofit.create(Service.class);
                return service;
            } else
            {
                return service;
            }
        }
    }

    class ImageFetcher
    {
        private static Service service;


        public static Service getInstance()
        {
            if (service == null)
            {
                Interceptor interceptor = chain -> {
                    Request newRequest = chain.request().newBuilder().build();

                    return chain.proceed(newRequest);
                };

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                Dispatcher dispatcher = new Dispatcher();
                dispatcher.setMaxRequests(1);
                builder.interceptors().add(interceptor);
                builder.dispatcher(dispatcher).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).cache(null);
                OkHttpClient client = builder.build();

                Gson gson = new GsonBuilder().setLenient().create();

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(client).build();

                service = retrofit.create(Service.class);
                return service;
            } else
            {
                return service;
            }
        }
    }

}
