package com.lumen.bikeme.commons.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map")
data class MapMarker(
    @ColumnInfo(name = "paramLong") val paramLong: Double,
    @ColumnInfo(name = "paramLat") val paramLat: Double,
    @ColumnInfo(name = "color") val markerColor: String,
    @PrimaryKey val id: String,
)