package com.lumen.bikeme.commons.repository.datasource

import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.repository.FirebaseService
import com.lumen.bikeme.commons.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firebaseService: FirebaseService,
    private val firebaseUserService: UserService
) : TripDataSource {

    override suspend fun listTrips(): List<TripItem> {

        val userId = firebaseUserService.getUserId()

        return firebaseService.getTrips(userId)?.map {
            TripItem(
                it.value.name,
                it.value.distance,
                it.value.date,
                it.key
            )
        } ?: emptyList()
    }

    override suspend fun insertTrip(tripItem: TripItem) {
        val userId = firebaseUserService.getUserId()

        firebaseService.saveTrip(tripItem, tripItem.id, userId)
    }

    override suspend fun deleteSingleTrip(id: String) {
        val userId = firebaseUserService.getUserId()

        firebaseService.deleteSingleTrip(id, userId)
    }
}