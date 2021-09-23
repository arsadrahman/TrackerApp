package com.arsa.trackerapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.arsa.trackerapp.R
import com.arsa.trackerapp.databinding.ActivityMainBinding
import com.arsa.trackerapp.services.TrackUserBackgroundServices
import com.arsa.trackerapp.util.Constants
import com.arsa.trackerapp.util.Constants.PERMISSION_REQUEST
import com.arsa.trackerapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var activityBinding:ActivityMainBinding
    val viewModel:MainViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()
        activityBinding.trackerState = TrackUserBackgroundServices.isTrackingActive.value
        viewModel.updateInternetConnectionStatus()
        checkInternetConnections()
        permissionAndRequestCheck()
    }

    @InternalCoroutinesApi
    private fun checkInternetConnections() {
        CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive){
                delay(1000)
                viewModel.updateInternetConnectionStatus()
                withContext(Dispatchers.Main){
                    activityBinding.connectionStatus = viewModel.internetStatus.value
                }
            }
        }




    }

    private fun permissionAndRequestCheck() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    PERMISSION_REQUEST
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         activityBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        activityBinding.activity = this

    }
    fun changeSwitch(state:Boolean){
        activityBinding.trackerState = state
        startOrStopService(state)
    }

    fun startOrStopService(state:Boolean){
        if(state){
            startService(Intent(this,TrackUserBackgroundServices::class.java).also {
                it.action = Constants.START_ACTION
            })
        }else{
            startService(Intent(this,TrackUserBackgroundServices::class.java).also {
                it.action = Constants.STOP_ACTION
            })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                    }
                } else {
                    permissionAndRequestCheck()
                }
                return
            }
        }
    }
}