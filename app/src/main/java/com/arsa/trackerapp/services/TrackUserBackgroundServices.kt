package com.arsa.trackerapp.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.arsa.trackerapp.R
import com.arsa.trackerapp.repo.LocationRepository
import com.arsa.trackerapp.repo.offline.OfflineRepository
import com.arsa.trackerapp.util.Constants
import com.arsa.trackerapp.util.InternetConnection
import com.arsa.trackerapp.view.MainActivity
import com.arsa.trackerapp.viewmodel.MainViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class TrackUserBackgroundServices : LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var notificationBuilder:NotificationCompat.Builder
    @Inject
    lateinit var locationRepository: LocationRepository
    @Inject
    lateinit var offlineRepository:OfflineRepository

    var isServiceRunning: Boolean = false

    companion object {
        val isTrackingActive = MutableLiveData<Boolean>()
        val updateLocation = MutableLiveData<Location>()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constants.START_ACTION -> {
                    if (isServiceRunning) {
                        //Service Resuming State
                    } else {
                        //New Service Created State
                        //Start tracking user with Foreground Services
                        startTrackingUserNow()
                    }
                }
                Constants.STOP_ACTION -> {
                    //Stop Service State
                    stopTrackingUser()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    fun startTrackingUserNow() {
        isServiceRunning = true
        isTrackingActive.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager as NotificationManager)
        }

        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())
        startTrackingLocation()
    }

    fun stopTrackingUser() {
        stopTrackingLocation()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager) {
        var channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_NAME,
            IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (isTrackingActive.value == true) {
                locationResult.locations.let { locations ->
                    for (location in locations) {
                        updateLocation.postValue(location)
                        uploadLocationToDataBase(location)
                    }
                }
            }
        }
    }

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(4)
        fastestInterval = TimeUnit.SECONDS.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    @SuppressLint("MissingPermission")
    fun startTrackingLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopTrackingLocation() {
        isServiceRunning = false
        isTrackingActive.postValue(false)
        stopForeground(true)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun uploadLocationToDataBase(location: Location){
        if(InternetConnection.isConnected(this))
            locationRepository.updateLocation(location)
        else
            offlineRepository.updateLocation(location)

    }


}