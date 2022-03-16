package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.model.TripItem
import retrofit2.http.*

interface FirebaseService {
    @GET("Trips/{userId}.json")
    suspend fun getTrips(@Path("userId") userId: String): Map<String, TripItem>?

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
}