package com.magicbluepenguin.repository.repositories

import android.content.Context
import androidx.room.Room
import com.magicbluepenguin.repository.api.RetrofitVenueSearchApiWrapper
import com.magicbluepenguin.repository.api.venuesearch.adapters.VenueDetailMoshiAdapter
import com.magicbluepenguin.repository.api.venuesearch.adapters.VenueListItemMoshiAdapter
import com.magicbluepenguin.repository.cache.VenueSearchDatabase
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class RetrofitVenueSearchRepository(
    context: Context,
    baseUrl: String,
    private val clientKey: String,
    private val clientSecret: String
) : VenueSearchRepository {

    private val retrofitServiceWrapper by lazy { getRetrofitServiceWrapperRetrofitServiceWrapper(baseUrl, clientKey, clientSecret) }

    private val venueSearchDao by lazy { getVenueSearchDao(context) }

    override suspend fun findVenuesNearLocation(location: String): RepositoryResponse<List<VenueListItem>> {
        try {
            val venues = retrofitServiceWrapper.listVenues(location)
            venueSearchDao.insertVenuesForQuery(location, venues)
            return SuccessResponse(venueSearchDao.getVenuesWithQuery(location)?.venueListItems)
        } catch (e: Exception) {
            return ErrorResponse(venueSearchDao.getVenuesWithQuery(location)?.venueListItems)
        }
    }

    override suspend fun getVenueDetails(venueId: String): RepositoryResponse<VenueDetail?> {
        try {
            val venueDetail = retrofitServiceWrapper.getVenueDetails(venueId)
            if (venueDetail != null) {
                venueSearchDao.insertVenueDetails(venueDetail)
            }
            return SuccessResponse(venueSearchDao.getVenueDetails(venueId))

        } catch (e: Exception) {
            return ErrorResponse(venueSearchDao.getVenueDetails(venueId))
        }
    }

    companion object {
        internal fun getRetrofitServiceWrapperRetrofitServiceWrapper(
            baseUrl: String,
            clientKey: String,
            clientSecret: String
        ): RetrofitVenueSearchApiWrapper {
            val moshi = Moshi.Builder()
                .add(VenueListItemMoshiAdapter)
                .add(VenueDetailMoshiAdapter)
                .build()
            return RetrofitVenueSearchApiWrapper(
                Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .baseUrl(baseUrl)
                    .build(), clientKey, clientSecret
            )
        }

        internal fun getVenueSearchDao(context: Context) = Room.databaseBuilder(context, VenueSearchDatabase::class.java, "RetrofitVenueSearchDB")
            .build()
            .locationSearchDao()
    }
}
