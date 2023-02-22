package com.magicbluepenguin.repository.repositories.venuesearch

import android.content.Context
import com.magicblueopenguin.cache.venuesearch.VenueSearchDaoWrapper
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.network.api.RetrofitVenueSearchApiWrapper
import com.magicbluepenguin.repository.response.RepositoryResponse
import com.magicbluepenguin.utils.extensions.isNetworkAvailable

interface VenueSearchRepository {

    suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>>

    suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?>
}

fun getCachedVenueSearchRepository(
    context: Context,
    baseUrl: String,
    clientKey: String,
    clientSecret: String
): VenueSearchRepository {

    return RetrofitVenueSearchRepository(
        VenueSearchDaoWrapper(context),
        RetrofitVenueSearchApiWrapper(baseUrl, clientKey, clientSecret)
    ) { context.isNetworkAvailable() }

}