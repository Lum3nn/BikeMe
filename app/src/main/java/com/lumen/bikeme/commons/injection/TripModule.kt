package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.commons.repository.*
import com.lumen.bikeme.commons.repository.datasource.FirebaseDataSource
import com.lumen.bikeme.commons.repository.datasource.TripDataSource
import com.lumen.bikeme.commons.repository.datasource.TripRoomDataSource
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
    fun provideRemoteDataSource(firebaseService: FirebaseService): TripDataSource {
        return FirebaseDataSource(firebaseService)
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
