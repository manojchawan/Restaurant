package com.manchaw.restaurant.network;

import com.manchaw.restaurant.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("geocode")
    Call<SearchResponse> getdata(@Query("lat") Double lat,
                                 @Query("lon") Double lon);
}
