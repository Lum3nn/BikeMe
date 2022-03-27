package com.lumen.bikeme.commons.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "trip")
data class TripItem(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "distance") val distance: String,
    @ColumnInfo(name = "date") val date: Date,
    @PrimaryKey val id: String,
) : TripItemList
