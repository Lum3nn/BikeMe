package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.commons.repository.TripRepository
import com.lumen.bikeme.commons.repository.TripResponseDao
import com.lumen.bikeme.commons.repository.TripRoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TripModule {

    @Provides
    @Singleton
    fun provideTripRoomRepository(dao: TripResponseDao): TripRepository {
        return TripRoomRepository(dao)
    }
}
