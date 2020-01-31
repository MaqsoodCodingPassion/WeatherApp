package com.openweathermap.org

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.openweathermap.org.gps.GpsUtils
import com.openweathermap.org.gps.LocationViewModel
import com.openweathermap.org.util.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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
        searchBtn.setOnClickListener{callCurrentWeatherAPI()}
        //getWeatherDataFromNetwork()
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
        weatherViewModel.fetchCurrentWeatherDetails(getCitiesList(),API_KEY).observe(this, Observer {
           hideShimmer()
       })
    }

    private fun getCitiesList(): ArrayList<String> {
        val values = citiesField.text.toString().split(",")
        var citiesList = ArrayList(values)
        return citiesList
    }

    /* @VisibleForTesting
     private fun updateUI(weatherEntity: WeatherEntity) {

     }*/

    private fun hideShimmer() {

    }

    private fun getWeatherDataFromNetwork() {
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
                callCurrentWeatherAPI()
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


