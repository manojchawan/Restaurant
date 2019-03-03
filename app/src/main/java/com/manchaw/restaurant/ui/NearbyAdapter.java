package com.manchaw.restaurant.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.manchaw.restaurant.R;
import com.manchaw.restaurant.model.NearbyRestaurant;
import com.manchaw.restaurant.util.GlideApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.RestaurantViewHolder> {

    private List<NearbyRestaurant> restaurantList;
    Context context;

    public NearbyAdapter(Context context, List<NearbyRestaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_nearby_restaurant, parent, false);
        return new RestaurantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        NearbyRestaurant restaurant = restaurantList.get(position);

        holder.name.setText(restaurant.getRestaurant().getName());
        holder.avgCost.setText(restaurant.getRestaurant().getAvgCost());
        holder.cuisine.setText(restaurant.getRestaurant().getCuisines());
        holder.rating.setText(restaurant.getRestaurant().getUserRating().getAggregateRating());
        holder.rating.setTextColor(Color.parseColor(restaurant.getRestaurant().getUserRating().getRatingColor()));
        holder.star.setColorFilter(Color.parseColor(restaurant.getRestaurant().getUserRating().getRatingColor()));

        GlideApp.with(context).load(restaurant.getRestaurant().getThumb())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_restaurant)
                .error(R.drawable.ic_restaurant)
                .into(holder.thumb);




    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView name, avgCost, cuisine, rating;
        ImageView thumb, star;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            avgCost = itemView.findViewById(R.id.avgCost);
            cuisine = itemView.findViewById(R.id.cuisine);
            thumb = itemView.findViewById(R.id.thumb);
            star = itemView.findViewById(R.id.star);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
