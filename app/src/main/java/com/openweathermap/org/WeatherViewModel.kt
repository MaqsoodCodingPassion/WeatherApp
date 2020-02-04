package com.openweathermap.org

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.Forecast5days3hoursResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    var compositeDisposable = CompositeDisposable()
    var currentWeatherResponse: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()  //Forecast5days3hoursResponse
    var forecast5days3hoursResponse: MutableLiveData<Forecast5days3hoursResponse> = MutableLiveData()


    /*
       Taking list with multiple cities and calling APIs in parallel
     */
    fun fetchMultipleCitiesWeatherData(
        citiesList: List<String?>,
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

    /*
      fetching current city name using current lat long API
     */
    fun fetchCurrentLocationDetails(lat: String, long: String, apiKey: String) {

        compositeDisposable += repository.fetchCurrentWeatherDetails(lat, long, apiKey)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<CurrentWeatherResponse>() {
                override fun onComplete() {
                }

                override fun onNext(data: CurrentWeatherResponse) {
                    currentWeatherResponse.value = data
                }

                override fun onError(e: Throwable) {
                    currentWeatherResponse.value = null
                }

            })
    }


    /*
      fetching 5 days and 3 hours each w.t.r city name
     */

    fun fetchForeCast5Days3HoursData(cityName: String, apiKey: String) {

        compositeDisposable += repository.fetchForecast5Days3Hours(cityName, apiKey)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Forecast5days3hoursResponse>() {
                override fun onComplete() {
                }

                override fun onNext(data: Forecast5days3hoursResponse) {
                    forecast5days3hoursResponse.value = data
                }

                override fun onError(e: Throwable) {
                    forecast5days3hoursResponse.value = null
                }

            })
    }

    /*
       Adding disposables using extension function
     */
    operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    /*
       clearing all disposables
     */
    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}

