package com.lumen.bikeme.commons.repository

import com.lumen.bikeme.commons.model.TripItem
import retrofit2.http.*

interface FirebaseService {
    @GET("Trips.json?")
    suspend fun getTrips(): Map<String, TripItem>?

    @PUT("Trips/{generatedId}.json")
    suspend fun saveTrip(@Body trip: TripItem, @Path("generatedId") id: String)

    @DELETE("Trips/{generatedId}.json")
    suspend fun deleteSingleTrip(@Path("generatedId") id: String)
}