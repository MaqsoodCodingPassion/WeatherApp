package com.openweathermap.org

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.openweathermap.org.R
import com.openweathermap.org.gps.GpsUtils
import com.openweathermap.org.gps.LocationViewModel
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.model.WeatherEntity
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
        getWeatherDataFromNetwork()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                getWeatherDataFromNetwork()
            }
        }
    }

    private fun callCurrentWeatherAPI(lat: String?,long: String?) {
       weatherViewModel.fetchCurrentWeatherDetails(lat!!,long!!,API_KEY).observe(this, Observer {
           hideShimmer()
       })
    }

    @VisibleForTesting
    private fun updateUI(weatherEntity: WeatherEntity) {
        mainContainer.showView(true)
        locationVal.text = weatherEntity.country
        latVal.text = weatherEntity.lat
        lonVal.text = weatherEntity.lon
        descriptionVal.text = weatherEntity.description
        tempVal.text = weatherEntity.temp
        visibilityVal.text = weatherEntity.visibility
        humidityVal.text = weatherEntity.humidity
        pressureVal.text = weatherEntity.pressure.toString()
        speedVal.text = weatherEntity.speed
        cloudVal.text = weatherEntity.main
        placeNameVal.text = weatherEntity.name
        var iconurl = "http://openweathermap.org/img/w/" + weatherEntity.icon + ".png";
        Glide.with(this)
            .load(iconurl)
            .into(iconVal)
    }

    private fun hideShimmer() {
        latLong.showView(false)
    }

    private fun getWeatherDataFromNetwork() {
        when {
            !isGPSEnabled -> {
                latLong.showView(true)
                mainContainer.showView(false)
                latLong.text = getString(R.string.enable_gps)}

            isPermissionsGranted(this) -> startLocationUpdate()
            shouldShowRequestPermissionRationale(this) -> latLong.text = getString(R.string.permission_request)

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
                callCurrentWeatherAPI(LAT,LONG)
                count++
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                getWeatherDataFromNetwork()
            }
        }
    }
}

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101


