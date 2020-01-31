package com.openweathermap.org.model

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName
class WeatherEntity {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("lat")
    var lat: String? = null

    @SerializedName("lon")
    var lon: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("temp")
    var temp: String? = null

    @SerializedName("main")
    var main: String? = null

    @SerializedName("visibility")
    var visibility: String? = null

    @SerializedName("humidity")
    var humidity: String? = null

    @SerializedName("pressure")
    var pressure: Int = 0

    @SerializedName("speed")
    var speed: String? = null

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("time")
    var time: Long = 0
}

