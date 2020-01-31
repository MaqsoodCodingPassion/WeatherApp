package com.openweathermap.org.service

import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.WeatherEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("weather")
    fun getWeatherDetails(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String)
            : Single<CurrentWeatherResponse>
}
