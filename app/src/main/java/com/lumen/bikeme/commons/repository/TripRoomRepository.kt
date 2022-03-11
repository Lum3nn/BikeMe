package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.model.TripItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRoomRepository @Inject constructor(private val tripResponseDao: TripResponseDao) :
    TripRepository {

    override suspend fun listTrips(): List<TripItem> {
        return tripResponseDao.fetchTrips()
    }

    override suspend fun insertTrip(tripItem: TripItem) {
        tripResponseDao.insertTrip(tripItem)
    }

    override suspend fun deleteAllTrips() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSingleTrip(id: Int) {
        tripResponseDao.deleteTrip(id)
    }
}
