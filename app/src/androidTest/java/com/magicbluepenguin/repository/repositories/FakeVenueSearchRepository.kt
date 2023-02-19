package com.magicbluepenguin.repository.repositories

import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem

class FakeVenueSearchRepository : VenueSearchRepository {
    override suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?> {
        TODO("Not yet implemented")
    }
}