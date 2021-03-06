package com.lumen.bikeme.commons.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lumen.bikeme.commons.model.TripItem

@Dao
interface TripResponseDao {

    @Query("SELECT * FROM trip ORDER BY date DESC ")
    suspend fun fetchTrips(): List<TripItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTrips(rateItems: List<TripItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(tripItem: TripItem)

    @Query("DELETE FROM trip")
    suspend fun deleteAllTrips()

    @Query("DELETE FROM trip WHERE id = :id ")
    suspend fun deleteTrip(id: String)
}
