package com.openweathermap.org.model

import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse {

    @SerializedName("visibility")
    var visibility: String? = null

    @SerializedName("timezone")
    var timezone: Int = 0

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("dt")
    var dt: Int = 0

    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("weather")
    var weather: List<WeatherItem>? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("cod")
    var cod: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("base")
    var base: String? = null

    @SerializedName("wind")
    var wind: Wind? = null
}

class Main {

    @SerializedName("temp")
    var temp: Double = 0.toDouble()

    @SerializedName("temp_min")
    var tempMin: Double = 0.toDouble()

    @SerializedName("humidity")
    var humidity: Int = 0

    @SerializedName("pressure")
    var pressure: Int = 0

    @SerializedName("temp_max")
    var tempMax: Double = 0.toDouble()
}

class WeatherItem {

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("main")
    var main: String? = null

    @SerializedName("id")
    var id: String? = null
}

class Wind {

    @SerializedName("deg")
    var deg: Int = 0

    @SerializedName("speed")
    var speed: Double = 0.toDouble()
}

class Sys {

    @SerializedName("country")
    var country: String? = null

    @SerializedName("sunrise")
    var sunrise: Int = 0

    @SerializedName("sunset")
    var sunset: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("type")
    var type: Int = 0
}

class Coord {

    @SerializedName("lon")
    var lon: Double = 0.toDouble()

    @SerializedName("lat")
    var lat: Double = 0.toDouble()
}

class Clouds {

    @SerializedName("all")
    var all: Int = 0
}