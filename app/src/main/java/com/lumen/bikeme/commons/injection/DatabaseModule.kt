package com.lumen.bikeme.commons.injection

import androidx.room.Room
import com.lumen.bikeme.commons.repository.AppDatabase
import com.lumen.bikeme.commons.repository.TripResponseDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "trips.db"
        ).build()
    }

    single<TripResponseDao> {
        get<AppDatabase>().tripListDao()
    }
}

//@InstallIn(SingletonComponent::class)
//@Module
//object DatabaseModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            AppDatabase::class.java,
//            "trips.db"
//        ).build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideRateListDao(database: AppDatabase): TripResponseDao {
//        return database.tripListDao()
//    }
//
//}
