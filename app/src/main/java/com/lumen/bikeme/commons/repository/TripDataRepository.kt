package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.PushIdGenerator
import com.lumen.bikeme.commons.injection.LocalDataSource
import com.lumen.bikeme.commons.injection.RemoteDataSource
import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.repository.datasource.TripDataSource
import com.lumen.bikeme.commons.toDate
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripDataRepository @Inject constructor(
    @LocalDataSource private val localDataSource: TripDataSource,
    @RemoteDataSource private val remoteDataSource: TripDataSource
) : TripRepository {

    override suspend fun listTrips(): List<TripItem> {
        val localData = localDataSource.listTrips()
        val remoteData = remoteDataSource.listTrips()

        if (localData.isEmpty() && remoteData.isEmpty()) {
            return emptyList()
        }

        when {
            localData == remoteData -> {
                return localData
            }
            localData.size > remoteData.size -> {
                localData.forEach {
                    remoteDataSource.insertTrip(it)
                }
                return localData
            }
            else -> {
                remoteData.forEach {
                    localDataSource.insertTrip(it)
                }
                return localDataSource.listTrips()
            }
        }
    }

    override suspend fun insertTrip(tripName: String, tripDistance: String, tripDate: String) {

        val generatedId = PushIdGenerator.generatePushChildName(Date().time)
        if (tripName.isEmpty() || tripDistance.isEmpty() || tripDate.isEmpty()) {
            throw TripRepository.EmptyFieldException()
        }

        val tripItem = TripItem(tripName, tripDistance, tripDate.toDate(), generatedId)
        remoteDataSource.insertTrip(tripItem)
        localDataSource.insertTrip(tripItem)
    }

    override suspend fun deleteSingleTrip(id: String) {
        localDataSource.deleteSingleTrip(id)
        remoteDataSource.deleteSingleTrip(id)
    }
}