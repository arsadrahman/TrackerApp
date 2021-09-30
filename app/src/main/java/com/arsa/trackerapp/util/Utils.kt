package com.arsa.trackerapp.util

import android.location.Location
import com.arsa.trackerapp.model.LocationModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

object Utils {
    fun locationToModel(location: Location):LocationModel{
        val geoPoint = GeoPoint(location.latitude,location.longitude)
        return LocationModel(0,geoPoint, Timestamp.now())

    }
}