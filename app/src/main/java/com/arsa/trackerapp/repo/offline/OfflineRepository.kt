package com.arsa.trackerapp.repo.offline

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.arsa.trackerapp.model.LocationModel
import com.arsa.trackerapp.util.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

class OfflineRepository @Inject constructor(val offlineDAO: OfflineDAO) {



    suspend fun getAllLocations() : Deferred<List<LocationModel>> =  CoroutineScope(Dispatchers.IO).async {
        return@async offlineDAO.getAllLocations()
    }

     fun updateLocation(location: Location) =
        CoroutineScope(Dispatchers.IO ).launch {
            offlineDAO.updateOfflineLocation(Utils.locationToModel(location))
        }

     suspend fun deleteAllLocation() = CoroutineScope(Dispatchers.IO ).async {
            return@async offlineDAO.deleteOfflineLocation()
        }

}