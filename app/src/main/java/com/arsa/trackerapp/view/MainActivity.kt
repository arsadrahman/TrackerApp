package com.arsa.trackerapp.view

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.arsa.trackerapp.R
import com.arsa.trackerapp.databinding.ActivityMainBinding
import com.arsa.trackerapp.services.TrackUserBackgroundServices
import com.arsa.trackerapp.util.Constants
import com.arsa.trackerapp.util.Constants.PERMISSION_REQUEST
import com.arsa.trackerapp.viewmodel.MainViewModel
import com.arsa.trackerapp.workmanager.BackgroundWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.ParsePosition
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()
        activityBinding.trackerState = TrackUserBackgroundServices.isTrackingActive.value
        viewModel.updateInternetConnectionStatus()
        checkInternetConnections()
        permissionAndRequestCheck()
        setupBackgroundWorkManager()

    }

    @InternalCoroutinesApi
    private fun checkInternetConnections() {
        CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                delay(1000)
                viewModel.updateInternetConnectionStatus()
                withContext(Dispatchers.Main) {
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
        setContentView(com.arsa.trackerapp.R.layout.activity_main)
        activityBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        activityBinding.activity = this

    }

    fun changeSwitch(state: Boolean) {
        activityBinding.trackerState = state
        startOrStopService(state)
    }

    fun startOrStopService(state: Boolean) {
        if (state) {
            startService(Intent(this, TrackUserBackgroundServices::class.java).also {
                it.action = Constants.START_ACTION
            })
        } else {
            startService(Intent(this, TrackUserBackgroundServices::class.java).also {
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

    fun setupBackgroundWorkManager() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(BackgroundWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints).addTag("locationop").build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

    fun notify(view: View?, buttonPosition: Int) {
        createAndPushNotification(buttonPosition)
    }
    fun openActivity(view:View?){
        startActivity(Intent(this,ActivityNavigationExample::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelID: String,
        notificationName: String,
        soundUri: Uri
    ) {

        var channel = NotificationChannel(
            channelID,
            notificationName,
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        var audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build();
        channel.setSound(soundUri, audioAttributes)
        notificationManager.createNotificationChannel(channel)

    }


    fun createAndPushNotification(position: Int) {
        lateinit var channelID: String
        lateinit var notificationName: String
        lateinit var soundUri: Uri
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when (position) {
            1 -> {
                channelID = Constants.NOTIFICATION_CHANNEL_ID_1
                notificationName = Constants.NOTIFICATION_NAME_1
                soundUri =
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.noti1)
            }
            2 -> {
                channelID = Constants.NOTIFICATION_CHANNEL_ID_2
                notificationName = Constants.NOTIFICATION_NAME_2
                soundUri =
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.noti2)
            }
            3 -> {
                channelID = Constants.NOTIFICATION_CHANNEL_ID_3
                notificationName = Constants.NOTIFICATION_NAME_3
                soundUri =
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.noti3)
            }
        }
        notificationBuilder.setContentTitle(notificationName)
            .setContentText("Notification one with sound")
            .setSound(soundUri).setChannelId(channelID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager, channelID, notificationName, soundUri)
        }
        notificationManager.notify(position, notificationBuilder.build())
    }

}