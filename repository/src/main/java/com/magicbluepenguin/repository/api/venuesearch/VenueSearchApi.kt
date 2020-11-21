package com.magicbluepenguin.repository.api.venuesearch

import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface VenueSearchApi {

    @GET("v2/venues/search")
    suspend fun listVenues(
        @Query("client_id")
        clientId: String,
        @Query("client_secret")
        clientSecret: String,
        @Query("near")
        location: String,
        @Query("radius")
        radius: Int,
        @Query("limit")
        limit: Int,
        @Query("v")
        version: String
    ): List<VenueListItem>

    @GET("v2/venues/{venueId}")
    suspend fun venueDetails(
        @Path("venueId")
        venueId: String,
        @Query("client_id")
        clientId: String,
        @Query("client_secret")
        clientSecret: String,
        @Query("v")
        version: String
    ): VenueDetail?
}