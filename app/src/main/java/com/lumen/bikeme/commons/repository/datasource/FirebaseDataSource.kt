package com.lumen.bikeme.commons.repository.datasource

import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.repository.FirebaseService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firebaseService: FirebaseService
) : TripDataSource {

    override suspend fun listTrips(): List<TripItem> {
        return firebaseService.getTrips()?.map {
            TripItem(
                it.value.name,
                it.value.distance,
                it.value.date,
                it.key
            )
        } ?: emptyList()
    }

    override suspend fun insertTrip(tripItem: TripItem) {
        firebaseService.saveTrip(tripItem, tripItem.id)
    }

    override suspend fun deleteSingleTrip(id: String) {
        firebaseService.deleteSingleTrip(id)
    }
}