package com.arsa.trackerapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.arsa.trackerapp.R
import com.arsa.trackerapp.util.Constants
import com.arsa.trackerapp.view.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext appContext: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(appContext, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // This may help to work in all devices notifications
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
    }


    @Provides
    fun providerMainActivityPendingIntent(@ApplicationContext appContext: Context): PendingIntent {
        val notificationIntent = Intent(
            appContext,
            MainActivity::class.java
        )
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(
            appContext,
            Constants.ACTIVITY_REQUEST_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}