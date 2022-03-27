package com.lumen.bikeme.commons.network

import com.lumen.bikeme.commons.model.MapMarker
import com.lumen.bikeme.commons.model.TripItem
import retrofit2.http.*

interface FirebaseService {

    @GET("Trips/{userId}.json")
    suspend fun getTrips(@Path("userId") userId: String): Map<String, TripItem>

    @PUT("Trips/{userId}/{generatedId}.json")
    suspend fun saveTrip(
        @Body trip: TripItem,
        @Path("generatedId") id: String,
        @Path("userId") userId: String
    )

    @DELETE("Trips/{userId}/{generatedId}.json")
    suspend fun deleteSingleTrip(
        @Path("generatedId") id: String,
        @Path("userId") userId: String
    )

    @PUT("Maps/{userId}/{generatedId}.json")
    suspend fun saveMarker(
        @Body mapMarker: MapMarker,
        @Path("generatedId") id: String,
        @Path("userId") userId: String
    )

    @GET("Maps/{userId}.json")
    suspend fun getMaps(@Path("userId") userId: String): Map<String, MapMarker>
}