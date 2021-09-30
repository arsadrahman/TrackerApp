package com.arsa.trackerapp.util

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson

class FirebaseTypeConverter {

    @TypeConverter
    fun stringToGeoPoint(data:String):GeoPoint{
        return Gson().fromJson(data,GeoPoint::class.java)
    }

    @TypeConverter
    fun geoPointToString(data:GeoPoint):String{
        return Gson().toJson(data)
    }


    @TypeConverter
    fun stringToTimestamp(data:String): Timestamp {
        return Gson().fromJson(data, Timestamp::class.java)
    }

    @TypeConverter
    fun timestampToString(data: Timestamp):String{
        return Gson().toJson(data)
    }

}