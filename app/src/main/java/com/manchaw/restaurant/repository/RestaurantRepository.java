package com.manchaw.restaurant.repository;

import android.content.res.Resources;
import android.view.View;

import com.manchaw.restaurant.R;
import com.manchaw.restaurant.model.SearchResponse;
import com.manchaw.restaurant.network.ApiClient;
import com.manchaw.restaurant.network.ApiInterface;
import com.manchaw.restaurant.util.UtilKt;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private static final String TAG = RestaurantRepository.class.getSimpleName();
    private ApiInterface apiInterface;

    public RestaurantRepository() {
    }

    public MutableLiveData<SearchResponse> getNearbyRestaurants(double lat, double lon) {
        final MutableLiveData<SearchResponse> responseMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResponse> call = apiInterface.getdata(lat, lon);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UtilKt.debugLog(TAG, "Response Success");
                    responseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                UtilKt.debugLog(TAG, "error" + t.getLocalizedMessage());
            }
        });

        return responseMutableLiveData;
    }


}
