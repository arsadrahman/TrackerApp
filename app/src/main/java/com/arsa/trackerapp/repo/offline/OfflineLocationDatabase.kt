package com.arsa.trackerapp.repo.offline

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arsa.trackerapp.model.LocationModel
import com.arsa.trackerapp.util.FirebaseTypeConverter

@Database(entities = [LocationModel::class], version = 1, exportSchema = false)
@TypeConverters(FirebaseTypeConverter::class)
abstract class OfflineLocationDatabase : RoomDatabase() {
    abstract fun offlineDAO(): OfflineDAO

    companion object{
        private var INSTANCE:OfflineLocationDatabase? = null
        fun getOfflineDB(context: Context):OfflineLocationDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,OfflineLocationDatabase::class.java,"offline_location").build()
            }
            return INSTANCE!!
        }

    }
}