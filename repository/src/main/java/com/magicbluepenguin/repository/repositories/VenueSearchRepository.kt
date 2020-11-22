package com.magicbluepenguin.repository.repositories

import android.content.Context
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem

interface VenueSearchRepository {

    suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>>

    suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?>
}

fun getCachedVenueSearchRepository(
    context: Context,
    baseUrl: String,
    clientKey: String,
    clientSecret: String
): VenueSearchRepository = RetrofitVenueSearchRepository(context, baseUrl, clientKey, clientSecret)