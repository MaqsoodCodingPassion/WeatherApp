package com.openweathermap.org.service

import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.FiveDaysForecastResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("weather")
    fun getCityWeatherDataService(@Query("q") lat: String, @Query("appid") appid: String)
            : Observable<CurrentWeatherResponse>

    @GET("weather")
    fun getCurrentWeatherDetailsService(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String)
            : Single<CurrentWeatherResponse>

    @GET("forecast")
    fun getForecast5Days3HoursService(@Query("q") lat: String, @Query("appid") appid: String)
            : Single<FiveDaysForecastResponse>
}