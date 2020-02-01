package com.openweathermap.org

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.FiveDaysForecastResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    fun fetchCurrentWeatherDetails(citiesList: ArrayList<String?>, apiKey: String): LiveData<List<CurrentWeatherResponse>> {

        val weatherResponse: MutableLiveData<List<CurrentWeatherResponse>>  = MutableLiveData()
       // Observable.concat()
        for(city in citiesList){
            val observable = repository.fetchCurrentWeatherDetails(city!!, apiKey)

            observable.map<CurrentWeatherResponse> {
                it
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        weatherResponse.value= listOf(it)
                    },
                    {
                        weatherResponse.value = null
                    })
        }

        return weatherResponse
    }

    fun fetchCurrentLocationDetails(
        lat: String, long: String, apiKey: String): MutableLiveData<CurrentWeatherResponse> {

        val weatherResponse: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
        val observable = repository.getCurrentWeatherDetails(lat, long, apiKey)

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

    fun fetchForeCast5Days3HoursData(cityName: String,apiKey: String): LiveData<FiveDaysForecastResponse> {

        val weatherResponse: MutableLiveData<FiveDaysForecastResponse> = MutableLiveData()
        val observable = repository.fetchForecast5Days3Hours(cityName, apiKey)

        observable.map<FiveDaysForecastResponse> {
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