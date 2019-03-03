package com.manchaw.restaurant.viewModal;

import com.manchaw.restaurant.model.SearchResponse;
import com.manchaw.restaurant.repository.RestaurantRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RestaurantViewModal extends ViewModel {

    private MutableLiveData<SearchResponse> data;
    private RestaurantRepository restaurantModel;

    public RestaurantViewModal() {
        restaurantModel = new RestaurantRepository();

    }

//    public void init(){
//        if(this.data != null) return;
//        data = restaurantModel.getNearbyRestaurants(lat, lon);
//    }

    public MutableLiveData<SearchResponse> getRestaurants(double lat, double lon){
        if(this.data == null)
            data = restaurantModel.getNearbyRestaurants(lat, lon);
        return this.data;
    }
}
