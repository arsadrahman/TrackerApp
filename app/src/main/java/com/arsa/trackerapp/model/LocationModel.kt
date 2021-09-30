package com.arsa.trackerapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint


@Entity(tableName = "offline_location")
data class LocationModel(@PrimaryKey(autoGenerate = true)
                          @get:Exclude val id:Int,val location: GeoPoint, val timestamp: Timestamp)


