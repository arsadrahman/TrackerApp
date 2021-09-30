package com.arsa.trackerapp.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.arsa.trackerapp.repo.LocationRepository
import com.arsa.trackerapp.repo.offline.OfflineRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltWorker
class BackgroundWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val locationRepository: LocationRepository,
    val offlineRepository: OfflineRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Log.e("Worker", "Worker Running")

        var locationList = offlineRepository.getAllLocations().await()
        if (locationList.count() > 0) {
            if (locationRepository.updateAllLocationModel(locationList).await()) {
                offlineRepository.deleteAllLocation()
                return Result.success()
            } else {
                return Result.failure()
            }
        } else {
            return Result.failure()
        }
    }
}