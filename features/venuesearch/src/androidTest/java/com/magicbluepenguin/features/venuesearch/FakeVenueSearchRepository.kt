package com.magicbluepenguin.features.venuesearch

import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.repositories.venuesearch.VenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.response.RepositoryResponse

class FakeVenueSearchRepository : VenueSearchRepository {
    override suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?> {
        TODO("Not yet implemented")
    }
}