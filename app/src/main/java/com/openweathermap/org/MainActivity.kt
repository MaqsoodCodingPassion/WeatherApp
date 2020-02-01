package com.openweathermap.org

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.openweathermap.org.gps.GpsUtils
import com.openweathermap.org.gps.LocationViewModel
import com.openweathermap.org.util.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationViewModel: LocationViewModel
    private var isGPSEnabled = false
    private var LAT:String? = null
    private var LONG:String? = null

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
        intiView()
        searchBtn.setOnClickListener{callCurrentWeatherAPI()}
        getCurrentCityNameFromAPI()
    }

    private fun intiView() {
        citiesField.onActionViewExpanded()
        citiesField.isIconified = true
        fiveDaysDataRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
               // getWeatherDataFromNetwork()
            }
        }
    }

    private fun callCurrentWeatherAPI() {
        if(getCitiesList().size in 3..7){
            weatherViewModel.fetchCurrentWeatherDetails(getCitiesList(),API_KEY).observe(this, Observer {
                hideShimmer()
            })
        }else if(getCitiesList().size < 3){
            Toast.makeText(this,"Please enter the minimum 3 cities separated with comma",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Please do not enter more than 7 cities",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocationDetailsAPI(lat: String?,long: String?) {
        weatherViewModel.fetchCurrentLocationDetails(lat!!,long!!,API_KEY).observe(this, Observer {
            if(it!=null){
                cityName.text = it.name.toString()
                getForcast5Days3HoursAPI(it.name)
            }
            hideShimmer()
        })
    }

    private fun getForcast5Days3HoursAPI(cityName: String?) {
        weatherViewModel.fetchForeCast5Days3HoursData(cityName!!,API_KEY).observe(this, Observer {
            if(it!=null){
                fiveDaysDataRecyclerView.adapter = Forecast5Days3HoursAdapter(it.list)
            }
            hideShimmer()
        })
    }

    private fun getCitiesList(): ArrayList<String?> {
        val values = citiesField.query.toString().split(",")
        return ArrayList<String?>(values)
    }

    /* @VisibleForTesting
     private fun updateUI(weatherEntity: WeatherEntity) {

     }*/

    private fun hideShimmer() {

    }

    private fun getCurrentCityNameFromAPI() {
        when {
            !isGPSEnabled -> {
                //show error
               // latLong.showView(true)
                //mainContainer.showView(false)
                //latLong.text = getString(R.string.enable_gps)
                }

            isPermissionsGranted(this) -> startLocationUpdate()
            shouldShowRequestPermissionRationale(this) -> "khan"
               // latLong.text = getString(R.string.permission_request)

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
            hideShimmer()
            LAT = it.latitude.toString()
            LONG = it.longitude.toString()
            if(count==0){
                getCurrentLocationDetailsAPI(LAT,LONG)
                count++
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                //getWeatherDataFromNetwork()
            }
        }
    }
}

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101


