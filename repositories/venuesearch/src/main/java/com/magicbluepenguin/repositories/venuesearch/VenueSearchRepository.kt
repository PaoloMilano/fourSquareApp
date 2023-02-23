package com.magicbluepenguin.repositories.venuesearch

import android.content.Context
import com.magicblueopenguin.cache.venuesearch.VenueSearchDaoWrapper
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.network.api.VenueSearchApiProvider
import com.magicbluepenguin.repositories.venuesearch.response.RepositoryResponse
import com.magicbluepenguin.utils.extensions.isNetworkAvailable

interface VenueSearchRepository {

    suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>>

    suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?>
}

fun getCachedVenueSearchRepository(context: Context): VenueSearchRepository {

    return CachedVenueSearchRepository(
        VenueSearchDaoWrapper(context),
        VenueSearchApiProvider.getWrapper()
    ) { context.isNetworkAvailable() }

}