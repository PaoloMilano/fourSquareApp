package com.magicbluepenguin.network.api

import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem

interface VenueSearchApiWrapper {

    suspend fun listVenues(
        location: String,
        radius: Int = 1000,
        limit: Int = 100
    ): List<VenueListItem>

    suspend fun getVenueDetails(venueId: String): VenueDetail?
}