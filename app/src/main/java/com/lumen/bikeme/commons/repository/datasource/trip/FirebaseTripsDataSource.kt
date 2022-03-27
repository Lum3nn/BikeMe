package com.lumen.bikeme.commons.repository.datasource.trip

import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.network.FirebaseService
import com.lumen.bikeme.commons.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTripsDataSource @Inject constructor(
    private val firebaseService: FirebaseService,
    private val firebaseUserService: UserService
) : TripDataSource {

    override suspend fun listTrips(): List<TripItem> {
        val userId = firebaseUserService.getUserId()
        return try {
            firebaseService.getTrips(userId).map {
                TripItem(
                    it.value.name,
                    it.value.distance,
                    it.value.date,
                    it.key
                )
            }
        } catch (e: KotlinNullPointerException) {
            emptyList()
        }
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