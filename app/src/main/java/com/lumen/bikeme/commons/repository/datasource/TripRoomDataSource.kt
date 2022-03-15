package com.lumen.bikeme.commons.repository.datasource

import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.repository.TripResponseDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRoomDataSource @Inject constructor(
    private val tripResponseDao: TripResponseDao
) : TripDataSource {

    override suspend fun listTrips(): List<TripItem> {
        return tripResponseDao.fetchTrips()
    }

    override suspend fun insertTrip(tripItem: TripItem) {
        tripResponseDao.insertTrip(tripItem)
    }

    override suspend fun deleteSingleTrip(id: String) {
        tripResponseDao.deleteTrip(id)
    }
}