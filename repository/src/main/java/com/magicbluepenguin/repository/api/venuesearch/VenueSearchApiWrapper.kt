package com.magicbluepenguin.repository.api.venuesearch

import com.magicbluepenguin.repository.model.VenueDetail

internal class VenueSearchApiWrapper(private val clientId: String, private val clientSecret: String, private val locationApi: VenueSearchApi) {
    suspend fun listVenues(
        location: String,
        radius: Int = 1000,
        limit: Int = 10,
        version: String = API_VERSION
    ) = locationApi.listVenues(clientId, clientSecret, location, radius, limit, version)

    suspend fun getVenueDetails(venueId: String): VenueDetail? = locationApi.venueDetails(venueId, clientId, clientSecret, API_VERSION)
}

private const val API_VERSION = "20201118"