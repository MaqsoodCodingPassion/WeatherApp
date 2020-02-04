package com.openweathermap.org

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.openweathermap.org.gps.GpsUtils
import com.openweathermap.org.gps.LocationViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.ListItem
import com.openweathermap.org.util.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationViewModel: LocationViewModel
    private var isGPSEnabled = false
    private var LAT:String? = null
    private var LONG:String? = null
    private var citiesWeatherList :ArrayList<CurrentWeatherResponse>? = null
    private var citiesAdapter : CitiesAdapter? = null
    private var foreCast5DaysDataList :ArrayList<ListItem>? = null
    private var foreCast5DaysAdapter : Forecast5Days3HoursAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        weatherViewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel::class.java)
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnable
            }
        })
        initAdapter()
        searchBtn.setOnClickListener{callCurrentWeatherAPI()}
        getCurrentCityNameFromAPI()
    }

    private fun initAdapter() {
        citiesWeatherList = ArrayList<CurrentWeatherResponse>()
        citiesAdapter = CitiesAdapter(citiesWeatherList!!)
        foreCast5DaysDataList = ArrayList<ListItem>()
        foreCast5DaysAdapter = Forecast5Days3HoursAdapter(foreCast5DaysDataList!!)

        citiesRecyclerView.layoutManager = LinearLayoutManager(this)
        citiesRecyclerView.adapter = citiesAdapter
        fiveDaysDataRecyclerView.layoutManager = LinearLayoutManager(this)
        fiveDaysDataRecyclerView.adapter = foreCast5DaysAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                getCurrentCityNameFromAPI()
            }
        }
    }

    private fun callCurrentWeatherAPI() {
        if(getCitiesList().size in 3..7){
            searchBtn.isEnabled = false
            citiesWeatherList?.clear()
            weatherViewModel.fetchMultipleCitiesWeatherData(getCitiesList(),API_KEY).observe(this, Observer {
                searchBtn.isEnabled = true
                hideKeyboard(this)
                if(it!=null){
                    citiesAdapter!!.setDataList(it)
                    citiesAdapter!!.notifyDataSetChanged()
                }else{
                    showErrorDialog(this,"Please enter the proper city name with separated comma")
                }
            })
        }else if(getCitiesList().size < 3){
            showErrorDialog(this,"Please enter the minimum 3 cities separated with comma")
        }else{
            showErrorDialog(this,"Please do not enter more than 7 cities")
        }
    }

    private fun currentLocationObservalableLiveData() {
        foreCast5DaysDataList?.clear()
        weatherViewModel.currentWeatherResponse.observe(this, Observer {
            if (it != null) {
                cityName.text = it.name.toString()
                weatherViewModel.fetchForeCast5Days3HoursData(it.name.toString(),API_KEY)
                forecast5Days3HourObservalableLiveData()
            }
        })
    }

    private fun forecast5Days3HourObservalableLiveData() {
        weatherViewModel.forecast5days3hoursResponse.observe(this, Observer {
            if(it!=null){
                progressBar.visibility = View.GONE
                it.list?.let { it1 -> foreCast5DaysAdapter?.setDataList(it1) }
                foreCast5DaysAdapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun getCitiesList(): ArrayList<String?> {
        val values = citiesField.query.toString().split(",")
        return ArrayList<String?>(values)
    }

    private fun getCurrentCityNameFromAPI() {
        when {
            isPermissionsGranted(this) -> startLocationUpdate()
            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        var count: Int =0
        locationViewModel.getLocationData().observe(this, Observer {
            LAT = it.latitude.toString()
            LONG = it.longitude.toString()
            if(count==0){
                weatherViewModel.fetchCurrentLocationDetails(LAT!!,LONG!!,API_KEY)
                currentLocationObservalableLiveData()
                count++
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                getCurrentCityNameFromAPI()
            }
        }
    }
}

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101


