package com.openweathermap.org

import androidx.lifecycle.LiveData
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.service.Service
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val service: Service) {

    fun fetchCurrentWeatherDetails(lat: String, long: String, apiKey: String): Single<CurrentWeatherResponse> {
        return service.getWeatherDetails(lat, long, apiKey)
    }
}
