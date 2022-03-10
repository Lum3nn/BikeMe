package com.lumen.bikeme.repository

import com.lumen.bikeme.tripList.TripItem

interface TripRepository {

    suspend fun listTrips(): List<TripItem>
    suspend fun insertTrip(tripItem : TripItem)
    suspend fun deleteAllTrips()
    suspend fun deleteSingleTrip(id : Int)
}