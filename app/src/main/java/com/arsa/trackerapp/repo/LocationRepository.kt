package com.arsa.trackerapp.repo

import android.content.Context
import android.location.Location
import android.util.Log
import com.arsa.trackerapp.model.LocationModel
import com.arsa.trackerapp.util.InternetConnection
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class LocationRepository @Inject constructor(@ApplicationContext val context: Context) {
    private val firebaseStore = FirebaseFirestore.getInstance()


    fun updateLocation(location: Location) {
        val geoPoint = GeoPoint(location.latitude,location.longitude)
        var model:LocationModel = LocationModel(geoPoint, Timestamp.now())
        firebaseStore.collection("locations").add(model).addOnSuccessListener {
            Log.e("Document ID", it.id)
        }

    }


}