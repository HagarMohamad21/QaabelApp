package com.Qaabel.org.model.Api;


import com.Qaabel.org.model.Api.Response.PlaceResponse;
import com.Qaabel.org.model.Api.Response.PlacesResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class PlaceRequest {

    private static final String TAG = "PlaceRequest";

    public static Retrofit client=null;
    public static Retrofit getClient(String Base_Url){
        if(client==null)
        {
            client= new Retrofit.Builder().
                    baseUrl(Base_Url).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        return client;
    }

    public interface IServer {


        @GET("nearbysearch/json?inputtype=textquery&fields=formatted_address,name")
        Call<PlacesResponse> getNearByPlaces(@Query("name") String query,
                                             @Query("key") String API_KEY,
                                             @Query("radius") String radius,
                                             @Query("location") String location);

        @GET("textsearch/json?inputtype=textquery&fields=name,formatted_address,geometry")
        Call<PlacesResponse> getPlaces (@Query("input") String query,
                                       @Query("key") String API_KEY);


    }



    public static IServer getAPI(String domainName){
        return getClient(domainName).create(IServer.class);
    }
    }
