package com.lumen.bikeme.commons.repository.datasource.trip

import com.lumen.bikeme.commons.model.MapMarker
import com.lumen.bikeme.commons.network.FirebaseService
import com.lumen.bikeme.commons.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseMapSource @Inject constructor(
    private val firebaseService: FirebaseService,
    private val firebaseUserService: UserService
) : MapDataSource {

    override suspend fun insertMapMarker(mapMarker: MapMarker) {
        val userId = firebaseUserService.getUserId()
        firebaseService.saveMarker(mapMarker, mapMarker.id, userId)
    }

    override suspend fun getMapMarkers(): List<MapMarker> {
        val userId = firebaseUserService.getUserId()
        return try {
            firebaseService.getMaps(userId).map {
                MapMarker(
                    it.value.paramLong,
                    it.value.paramLat,
                    it.value.markerColor,
                    it.key
                )
            }
        } catch (e: KotlinNullPointerException) {
            emptyList()
        }
    }
}