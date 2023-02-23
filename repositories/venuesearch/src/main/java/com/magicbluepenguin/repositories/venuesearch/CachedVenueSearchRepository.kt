package com.magicbluepenguin.repositories.venuesearch

import com.magicblueopenguin.cache.venuesearch.VenueSearchDaoWrapper
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.network.api.VenueSearchApiWrapper
import com.magicbluepenguin.repositories.venuesearch.response.NetworkError
import com.magicbluepenguin.repositories.venuesearch.response.RepositoryResponse
import com.magicbluepenguin.repositories.venuesearch.response.ServerError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CachedVenueSearchRepository(
    private val venueSearchDao: VenueSearchDaoWrapper,
    private val retrofitServiceWrapper: VenueSearchApiWrapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val isNetworkAvailable: () -> Boolean
) : VenueSearchRepository {

    override suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>> = withContext(dispatcher) {
        try {
            val venues = retrofitServiceWrapper.listVenues(location)
            venueSearchDao.insertVenuesForQuery(location, venues)
            RepositoryResponse(venueSearchDao.getVenuesForQuery(location))
        } catch (e: Exception) {
            RepositoryResponse(venueSearchDao.getVenuesForQuery(location), getErrorCause())
        }
    }

    override suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?> = withContext(dispatcher) {
        try {
            val venueDetail = retrofitServiceWrapper.getVenueDetails(venueId)
            if (venueDetail != null) {
                venueSearchDao.insertVenueDetails(venueDetail)
            }
            RepositoryResponse(venueSearchDao.getVenueDetails(venueId))

        } catch (e: Exception) {
            RepositoryResponse(venueSearchDao.getVenueDetails(venueId), getErrorCause())
        }
    }

    private fun getErrorCause() = if (isNetworkAvailable()) {
        ServerError
    } else {
        NetworkError
    }
}
