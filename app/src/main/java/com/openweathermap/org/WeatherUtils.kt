package com.openweathermap.org

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.view.View
import androidx.core.app.ActivityCompat

val API_KEY = "5ad7218f2e11df834b0eaf3a33a39d2a"

//check for the time difference
fun getTimeDiff(currentTime: Long, previousTime: Long): Long {
    var diff = currentTime - previousTime
    return (diff / (60 * 60 * 1000))
}

//check for the internet connection
fun isNetworkConnected(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
}

//Extension function for visibility view
fun View.showView(show: Boolean) {
    visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun isPermissionsGranted(context: Context) =
    ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

fun shouldShowRequestPermissionRationale(activity: Activity) =
    ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) && ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )