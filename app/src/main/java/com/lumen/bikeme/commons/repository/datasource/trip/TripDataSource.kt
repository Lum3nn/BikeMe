package com.lumen.bikeme.commons.repository.datasource.trip

import com.lumen.bikeme.commons.model.TripItem

interface TripDataSource {

    suspend fun listTrips(): List<TripItem>
    suspend fun insertTrip(tripItem: TripItem)
    suspend fun deleteSingleTrip(id: String)
}