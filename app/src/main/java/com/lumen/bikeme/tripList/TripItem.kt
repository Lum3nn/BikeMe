package com.lumen.bikeme.tripList

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "trip")
data class TripItem(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "distance") val distance: String,
    @ColumnInfo(name = "date") val date: Date,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    ) : TripItemList
