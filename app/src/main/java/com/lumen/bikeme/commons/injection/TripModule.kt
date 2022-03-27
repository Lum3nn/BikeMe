package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.commons.network.FirebaseService
import com.lumen.bikeme.commons.repository.TripDataRepository
import com.lumen.bikeme.commons.repository.TripRepository
import com.lumen.bikeme.commons.repository.dao.TripResponseDao
import com.lumen.bikeme.commons.repository.datasource.trip.FirebaseTripsDataSource
import com.lumen.bikeme.commons.repository.datasource.trip.TripDataSource
import com.lumen.bikeme.commons.repository.datasource.trip.TripRoomDataSource
import com.lumen.bikeme.commons.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource

@InstallIn(SingletonComponent::class)
@Module
object TripModule {

    @Singleton
    @LocalDataSource
    @Provides
    fun provideLocalDataSource(dao: TripResponseDao): TripDataSource {
        return TripRoomDataSource(dao)
    }

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(
        firebaseService: FirebaseService,
        firebaseUserService: UserService
    ): TripDataSource {
        return FirebaseTripsDataSource(firebaseService, firebaseUserService)
    }

    @Provides
    @Singleton
    fun provideTripDataRepository(
        @LocalDataSource localDataSource: TripDataSource,
        @RemoteDataSource remoteDataSource: TripDataSource
    ): TripRepository {
        return TripDataRepository(localDataSource, remoteDataSource)
    }
}
