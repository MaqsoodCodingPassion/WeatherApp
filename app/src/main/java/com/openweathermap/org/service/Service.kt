package com.openweathermap.org.service

import com.openweathermap.org.model.CurrentWeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("weather")
    fun getWeatherDetails(@Query("q") lat: String, @Query("appid") appid: String)
            : Single<CurrentWeatherResponse>
}
