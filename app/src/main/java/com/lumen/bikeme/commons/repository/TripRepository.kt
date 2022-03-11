package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.model.TripItem

interface TripRepository {

    suspend fun listTrips(): List<TripItem>
    suspend fun insertTrip(tripItem : TripItem)
    suspend fun deleteAllTrips()
    suspend fun deleteSingleTrip(id : Int)
}