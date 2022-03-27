package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.commons.network.FirebaseService
import com.lumen.bikeme.commons.repository.MapDataRepository
import com.lumen.bikeme.commons.repository.MapRepository
import com.lumen.bikeme.commons.repository.dao.MapResponseDao
import com.lumen.bikeme.commons.repository.datasource.trip.FirebaseMapSource
import com.lumen.bikeme.commons.repository.datasource.trip.MapDataSource
import com.lumen.bikeme.commons.repository.datasource.trip.MapRoomDataSource
import com.lumen.bikeme.commons.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalMapDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteMapDataSource

@InstallIn(SingletonComponent::class)
@Module
object MapModule {

    @Singleton
    @LocalMapDataSource
    @Provides
    fun provideMapLocalDataSource(dao: MapResponseDao): MapDataSource {
        return MapRoomDataSource(dao)
    }

    @Singleton
    @RemoteMapDataSource
    @Provides
    fun provideMapRemoteDataSource(
        firebaseService: FirebaseService,
        firebaseUserService: UserService
    ): MapDataSource {
        return FirebaseMapSource(firebaseService, firebaseUserService)
    }

    @Provides
    @Singleton
    fun provideMapDataRepository(
        @LocalMapDataSource localMapDataSource: MapDataSource,
        @RemoteMapDataSource remoteMapDataSource: MapDataSource
    ): MapRepository {
        return MapDataRepository(localMapDataSource, remoteMapDataSource)
    }

}