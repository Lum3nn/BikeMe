package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.model.MapMarker

interface MapRepository {
    suspend fun addMark(paramLong: Double, paramLat: Double, color: String)
    suspend fun listMaps(): List<MapMarker>

}