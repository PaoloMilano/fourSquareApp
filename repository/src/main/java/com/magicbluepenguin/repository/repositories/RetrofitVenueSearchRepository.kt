package com.magicbluepenguin.repository.repositories

import com.magicbluepenguin.repository.api.RetrofitVenueSearchApiWrapper
import com.magicbluepenguin.repository.cache.VenueSearchDao
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem

internal class RetrofitVenueSearchRepository(
    private val venueSearchDao: VenueSearchDao,
    private val retrofitServiceWrapper: RetrofitVenueSearchApiWrapper,
) : VenueSearchRepository {

    override suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>> {
        return try {
            val venues = retrofitServiceWrapper.listVenues(location)
            venueSearchDao.insertVenuesForQuery(location, venues)
            SuccessResponse(venueSearchDao.getVenuesWithQuery(location)?.venueListItems)
        } catch (e: Exception) {
            ErrorResponse(venueSearchDao.getVenuesWithQuery(location)?.venueListItems)
        }
    }

    override suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?> {
        return try {
            val venueDetail = retrofitServiceWrapper.getVenueDetails(venueId)
            if (venueDetail != null) {
                venueSearchDao.insertVenueDetails(venueDetail)
            }
            SuccessResponse(venueSearchDao.getVenueDetails(venueId))

        } catch (e: Exception) {
            ErrorResponse(venueSearchDao.getVenueDetails(venueId))
        }
    }
}
