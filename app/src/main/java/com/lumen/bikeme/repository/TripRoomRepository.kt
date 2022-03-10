package com.lumen.bikeme.repository

import android.content.Context
import com.lumen.bikeme.network.AppDatabase
import com.lumen.bikeme.tripList.TripItem

class TripRoomRepository(applicationContext: Context) :
    TripRepository {

    private var roomDatabase: TripResponseDao =
        AppDatabase.getRoomDatabase(applicationContext).tripListDao()

    override suspend fun listTrips(): List<TripItem> {
        return roomDatabase.fetchTrips()
    }

    override suspend fun insertTrip(tripItem: TripItem) {
        roomDatabase.insertTrip(tripItem)
    }

    override suspend fun deleteAllTrips() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSingleTrip(id: Int) {
        roomDatabase.deleteTrip(id)
    }
}
