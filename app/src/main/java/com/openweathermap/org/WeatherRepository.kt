package com.openweathermap.org

import androidx.lifecycle.LiveData
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.service.Service
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val service: Service) {

    fun fetchCurrentWeatherDetails(cityName: String, apiKey: String): Single<CurrentWeatherResponse> {
        return service.getWeatherDetails(cityName, apiKey)
    }

    fun getCurrentWeatherDetails(lat: String, long: String, apiKey: String): Single<CurrentWeatherResponse> {
        return service.getCurrentWeatherDetails(lat, long, apiKey)
    }
}
