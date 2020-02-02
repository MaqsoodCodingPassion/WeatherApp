package com.openweathermap.org.service

import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.FiveDaysForecastResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("weather")
    fun getWeatherDetails(@Query("q") lat: String, @Query("appid") appid: String)
            : Observable<CurrentWeatherResponse>

    @GET("weather")
    fun getCurrentWeatherDetails(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String)
            : Single<CurrentWeatherResponse>

    @GET("forecast")
    fun getForecast5Days3Hours(@Query("q") lat: String,@Query("appid") appid: String)
            : Single<FiveDaysForecastResponse>
}