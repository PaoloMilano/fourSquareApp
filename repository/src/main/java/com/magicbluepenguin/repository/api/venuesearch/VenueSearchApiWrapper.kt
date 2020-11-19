package com.magicbluepenguin.repository.api.venuesearch

internal class VenueSearchApiWrapper(private val clientId: String, private val clientSecret: String, private val locationApi: VenueSearchApi) {
    suspend fun listVenues(
        location: String,
        radius: Int = 1000,
        limit: Int = 10,
        version: String = "20201118"
    ) = locationApi.listVenues(clientId, clientSecret, location, radius, limit, version).response.venues
}