package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.model.TripItem
import java.lang.Exception

interface TripRepository {

    suspend fun listTrips(): List<TripItem>
    suspend fun insertTrip(tripName: String, tripDistance: String, tripDate: String)
    suspend fun deleteSingleTrip(id: String)

    class EmptyFieldException() : Exception()
}