package com.openweathermap.org

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.Forecast5days3hoursResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    fun fetchMultipleCitiesWeatherData(
        citiesList: ArrayList<String?>,
        apiKey: String
    ): LiveData<List<CurrentWeatherResponse>> {

        val weatherResponse: MutableLiveData<List<CurrentWeatherResponse>> = MutableLiveData()

        Observable.fromIterable(citiesList).flatMap {
            repository.fetchCityWeatherData(it, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }.toList()
            .subscribe(
                {
                    weatherResponse.value = it
                }, {
                    it.printStackTrace()
                    weatherResponse.value = null
                }
            )
        return weatherResponse
    }

    fun fetchCurrentLocationDetails(
        lat: String, long: String, apiKey: String
    ): MutableLiveData<CurrentWeatherResponse> {

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

    fun fetchForeCast5Days3HoursData(
        cityName: String,
        apiKey: String
    ): LiveData<Forecast5days3hoursResponse> {

        val weatherResponse: MutableLiveData<Forecast5days3hoursResponse> = MutableLiveData()
        val observable = repository.fetchForecast5Days3Hours(cityName, apiKey)

        observable.map<Forecast5days3hoursResponse> {
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