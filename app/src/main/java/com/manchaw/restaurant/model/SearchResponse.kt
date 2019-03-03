package com.manchaw.restaurant.model


import com.google.gson.annotations.SerializedName

data class SearchResponse(
        @SerializedName("location") val location: Location,
        @SerializedName("nearby_restaurants") val nearbyRestaurants: List<NearbyRestaurant>

)

data class Location(
        @SerializedName("title") val title: String,
        @SerializedName("entity_id") val entityId: Int,
        @SerializedName("city_name") val cityName: String,
        @SerializedName("country_name") val country: String
)

data class NearbyRestaurant(
        @SerializedName("restaurant") val restaurant: Restaurant
)

class Restaurant(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("cuisines") val cuisines: String,
        @SerializedName("thumb") val thumb: String,
        @SerializedName("average_cost_for_two") private val averageCostForTwo: Int,
        @SerializedName("user_rating") val userRating: UserRating
) {
    var avgCost: String = ""
        get() {
            return "₹$averageCostForTwo for two"
        }
}


data class UserRating(
        @SerializedName("aggregate_rating") var aggregateRating: String? = null,
        @SerializedName("rating_color") private var rating_color: String? = null,
        @SerializedName("rating_text") var ratingText: String? = null
) {
    var ratingColor: String = ""
        get() {
            return "#$rating_color"
        }
}