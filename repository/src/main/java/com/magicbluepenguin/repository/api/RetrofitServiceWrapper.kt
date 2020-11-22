package com.magicbluepenguin.repository.api

import com.magicbluepenguin.repository.api.venuesearch.VenueSearchApi
import com.magicbluepenguin.repository.model.VenueDetail
import retrofit2.Retrofit

internal class RetrofitVenueSearchApiWrapper(private val retrofit: Retrofit, private val clientId: String, private val clientSecret: String) {

    private val venueSearchApi = retrofit.create(VenueSearchApi::class.java)

    suspend fun listVenues(
        location: String,
        radius: Int = 1000,
        limit: Int = 10,
        version: String = API_VERSION
    ) = venueSearchApi.listVenues(clientId, clientSecret, location, radius, limit, version)

    suspend fun getVenueDetails(venueId: String): VenueDetail? = venueSearchApi.venueDetails(venueId, clientId, clientSecret, API_VERSION)

}

private const val API_VERSION = "20201118"
