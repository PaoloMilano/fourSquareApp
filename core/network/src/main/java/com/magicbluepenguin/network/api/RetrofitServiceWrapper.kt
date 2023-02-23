package com.magicbluepenguin.network.api

import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.network.api.venuesearch.VenueSearchApi
import retrofit2.Retrofit

internal class RetrofitVenueSearchApiWrapper(retrofit: Retrofit, private val clientId: String, private val clientSecret: String) :
    VenueSearchApiWrapper {

    private val venueSearchApi = retrofit.create(VenueSearchApi::class.java)

    override suspend fun listVenues(
        location: String,
        radius: Int,
        limit: Int
    ) = venueSearchApi.listVenues(clientId, clientSecret, location, radius, limit, API_VERSION)

    override suspend fun getVenueDetails(venueId: String): VenueDetail? = venueSearchApi.venueDetails(venueId, clientId, clientSecret, API_VERSION)

}

private const val API_VERSION = "20201118"