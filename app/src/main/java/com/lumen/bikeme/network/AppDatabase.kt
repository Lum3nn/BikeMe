package com.lumen.bikeme.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lumen.bikeme.Converters
import com.lumen.bikeme.repository.TripResponseDao
import com.lumen.bikeme.tripList.TripItem

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

    companion object {

        private var instance: AppDatabase? = null

        fun getRoomDatabase(context: Context): AppDatabase {

            // 2nd way
//            return instance ?: Room.databaseBuilder(
//                context,
//                AppDatabase::class.java,
//                "trips.db"
//            ).build().also {
//                instance = it
//            }


            val tempInstance = instance

            return if (tempInstance != null) {
                tempInstance
            } else {
                val roomInstance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "trips.db"
                ).build()

                instance = roomInstance
                roomInstance
            }
        }
    }
}


