package com.lumen.bikeme.commons.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lumen.bikeme.commons.Converters
import com.lumen.bikeme.commons.model.TripItem

@Database(
    entities = [
        TripItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripListDao(): TripResponseDao
}


