package com.arsa.trackerapp.repo

import android.content.Context
import android.location.Location
import android.util.Log
import com.arsa.trackerapp.model.LocationModel
import com.arsa.trackerapp.util.InternetConnection
import com.arsa.trackerapp.util.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class LocationRepository @Inject constructor(@ApplicationContext val context: Context) {
    private val firebaseStore = FirebaseFirestore.getInstance()

    fun updateLocation(location: Location) = CoroutineScope(Dispatchers.IO).async {
        return@async firebaseStore.collection("locations").add(Utils.locationToModel(location)).await()
    }

     fun updateAllLocationModel(locations: List<LocationModel>) = CoroutineScope(Dispatchers.IO).async {
            //Firebase Batch Write
            val batch = firebaseStore.batch()
            for (location in locations) {
                var document = firebaseStore.collection("locations").document()
                batch.set(document, location)
            }
            return@async batch.commit().isSuccessful
        }




}