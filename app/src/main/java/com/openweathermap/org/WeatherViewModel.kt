package com.openweathermap.org

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.WeatherEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    fun fetchCurrentWeatherDetails(
        lat: String, long: String, apiKey: String): MutableLiveData<CurrentWeatherResponse> {

        val weatherResponse: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
        val observable = repository.fetchCurrentWeatherDetails(lat, long, apiKey)

        observable.map<CurrentWeatherResponse> {
            it
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    weatherResponse.value = it
                },
                {
                    weatherResponse.value = null
                })

        return weatherResponse
    }
}