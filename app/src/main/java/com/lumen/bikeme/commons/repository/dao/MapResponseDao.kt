package com.lumen.bikeme.commons.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lumen.bikeme.commons.model.MapMarker

@Dao
interface MapResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(mapMarker: MapMarker)

    @Query("SELECT * FROM map")
    suspend fun fetchMaps(): List<MapMarker>
}