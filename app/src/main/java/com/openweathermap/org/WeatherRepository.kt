package com.openweathermap.org

import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.FiveDaysForecastResponse
import com.openweathermap.org.service.Service
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val service: Service) {

    fun fetchCityWeatherData(cityName: String, apiKey: String): Observable<CurrentWeatherResponse> {
        return service.getCityWeatherDataService(cityName, apiKey)
    }

    fun fetchCurrentWeatherDetails(lat: String, long: String, apiKey: String): Single<CurrentWeatherResponse> {
        return service.getCurrentWeatherDetailsService(lat, long, apiKey)
    }

    fun fetchForecast5Days3Hours(cityName: String, apiKey: String): Single<FiveDaysForecastResponse> {
        return service.getForecast5Days3HoursService(cityName, apiKey)
    }
}
