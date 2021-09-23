package com.arsa.trackerapp.model

import android.location.Location
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class LocationModel(val location: GeoPoint,val timestamp: Timestamp)
