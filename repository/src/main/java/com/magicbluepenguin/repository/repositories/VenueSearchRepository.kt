package com.magicbluepenguin.repository.repositories

import android.content.Context
import androidx.room.Room
import com.magicbluepenguin.repository.api.RetrofitServiceWrapper
import com.magicbluepenguin.repository.api.venuesearch.adapters.VenueDetailMoshiAdapter
import com.magicbluepenguin.repository.api.venuesearch.adapters.VenueListItemMoshiAdapter
import com.magicbluepenguin.repository.cache.VenueSearchDatabase
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface VenueSearchRepository {
    suspend fun findVenuesNearLocation(location: String): List<VenueListItem>

    suspend fun getVenueDetails(venueId: String): VenueDetail?
}

class RetrofitVenueSearchRepository(
    context: Context,
    baseUrl: String,
    private val clientKey: String,
    private val clientSecret: String
) : VenueSearchRepository {

    private val retrofitServiceWrapper by lazy { getRetrofitServiceWrapperRetrofitServiceWrapper(baseUrl, clientKey, clientSecret) }

    private val venueSearchDao by lazy { getVenueSearchDao(context) }

    override suspend fun findVenuesNearLocation(location: String): List<VenueListItem> {
        val venues = retrofitServiceWrapper.getVenueSearchApiWrapper().listVenues(location)
        venueSearchDao.insertVenuesForQuery(location, venues)
        return venueSearchDao.getVenuesWithQuery(location).venueListItems
    }

    override suspend fun getVenueDetails(venueId: String): VenueDetail? {
        val venueDetail = retrofitServiceWrapper.getVenueSearchApiWrapper().getVenueDetails(venueId)
        if (venueDetail != null) {
            venueSearchDao.insertVenueDetails(venueDetail)
            return venueSearchDao.getVenueDetails(venueId)
        }
        return null
    }


    companion object {
        internal fun getRetrofitServiceWrapperRetrofitServiceWrapper(
            baseUrl: String,
            clientKey: String,
            clientSecret: String
        ): RetrofitServiceWrapper {
            val moshi = Moshi.Builder()
                .add(VenueListItemMoshiAdapter)
                .add(VenueDetailMoshiAdapter)
                .build()
            return RetrofitServiceWrapper(
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