package com.lumen.bikeme.commons.repository.datasource.trip

import com.lumen.bikeme.commons.model.MapMarker
import com.lumen.bikeme.commons.repository.dao.MapResponseDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRoomDataSource @Inject constructor(
    private val mapRoomResponseDao: MapResponseDao
) : MapDataSource {
    override suspend fun insertMapMarker(mapMarker: MapMarker) {
        mapRoomResponseDao.insertTrip(mapMarker)
    }

    override suspend fun getMapMarkers(): List<MapMarker> {
        return mapRoomResponseDao.fetchMaps()
    }
}