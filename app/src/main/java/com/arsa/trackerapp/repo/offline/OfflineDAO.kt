package com.arsa.trackerapp.repo.offline

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.arsa.trackerapp.model.LocationModel

@Dao
interface OfflineDAO {

    @Query("SELECT * FROM offline_location")
     suspend fun getAllLocations():List<LocationModel>

    @Insert
    suspend fun updateOfflineLocation(locationModel: LocationModel)

    @Query("DELETE FROM offline_location")
    suspend fun deleteOfflineLocation()

}