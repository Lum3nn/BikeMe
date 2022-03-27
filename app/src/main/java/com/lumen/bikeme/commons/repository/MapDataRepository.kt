package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.PushIdGenerator
import com.lumen.bikeme.commons.injection.LocalMapDataSource
import com.lumen.bikeme.commons.injection.RemoteMapDataSource
import com.lumen.bikeme.commons.model.MapMarker
import com.lumen.bikeme.commons.repository.datasource.trip.MapDataSource
import java.util.*
import javax.inject.Inject

class MapDataRepository @Inject constructor(
    @LocalMapDataSource private val localMapRoomRepository: MapDataSource,
    @RemoteMapDataSource private val remoteDataSource: MapDataSource
) : MapRepository {

    override suspend fun addMark(paramLong: Double, paramLat: Double, color: String) {
        val generatedId = PushIdGenerator.generatePushChildName(Date().time)
        val markMapParam = MapMarker(paramLong, paramLat, color, generatedId)
        localMapRoomRepository.insertMapMarker(markMapParam)
        remoteDataSource.insertMapMarker(markMapParam)
    }

    override suspend fun listMaps(): List<MapMarker> {
        val localData = localMapRoomRepository.getMapMarkers()
        val remoteData = remoteDataSource.getMapMarkers()

        if (localData.isEmpty() && remoteData.isEmpty()) {
            return emptyList()
        }

        when {
            localData == remoteData -> {
                return localData
            }
            localData.size > remoteData.size -> {
                localData.forEach {
                    remoteDataSource.insertMapMarker(it)
                }
                return localData
            }
            else -> {
                remoteData.forEach {
                    localMapRoomRepository.insertMapMarker(it)
                }
                return localMapRoomRepository.getMapMarkers()
            }
        }
    }
}