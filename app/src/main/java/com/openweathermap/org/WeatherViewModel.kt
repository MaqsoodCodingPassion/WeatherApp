package com.openweathermap.org

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.internal.operators.observable.ObservableReplay.observeOn
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    fun fetchCurrentWeatherDetails(citiesList: List<String>,apiKey: String): LiveData<List<CurrentWeatherResponse>> {

        val weatherResponse: MutableLiveData<List<CurrentWeatherResponse>>  = MutableLiveData()
       // Observable.concat()

        for(city in citiesList){
            val observable = repository.fetchCurrentWeatherDetails(city, apiKey)

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
}