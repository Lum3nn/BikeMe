package com.lumen.bikeme.commons.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lumen.bikeme.commons.model.MapMarker
import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.repository.dao.MapResponseDao
import com.lumen.bikeme.commons.repository.dao.TripResponseDao

@Database(
    entities = [
        TripItem::class,
        MapMarker::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripListDao(): TripResponseDao
    abstract fun mapDao(): MapResponseDao
}



