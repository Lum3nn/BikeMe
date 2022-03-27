package com.lumen.bikeme.commons.repository.datasource.trip

import com.lumen.bikeme.commons.model.MapMarker

interface MapDataSource {
    suspend fun insertMapMarker(mapMarker: MapMarker)
    suspend fun getMapMarkers(): List<MapMarker>
}