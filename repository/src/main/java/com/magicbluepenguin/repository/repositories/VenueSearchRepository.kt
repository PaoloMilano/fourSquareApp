package com.magicbluepenguin.repository.repositories

import android.content.Context
import androidx.room.Room
import com.magicbluepenguin.repository.api.RetrofitServiceWrapper
import com.magicbluepenguin.repository.cache.VenueSearchDatabase
import com.magicbluepenguin.repository.model.Venue
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface VenueSearchRepository {
    suspend fun findVenuesNearLocation(location: String): List<Venue>
}

class RetrofitVenueSearchRepository(
    context: Context,
    baseUrl: String,
    private val clientKey: String,
    private val clientSecret: String
) : VenueSearchRepository {

    private val retrofitServiceWrapper by lazy { getRetrofitServiceWrapperRetrofitServiceWrapper(baseUrl, clientKey, clientSecret) }

    private val venueSearchDao by lazy { getVenueSearchDao(context) }

    override suspend fun findVenuesNearLocation(location: String): List<Venue> {
        val venues = retrofitServiceWrapper.getVenueSearchApiWrapper().listVenues(location)
        venueSearchDao.insertVenuesForQuery(location, venues)
        return venueSearchDao.getVenuesWithQuery(location).venues
    }

    companion object {
        internal fun getRetrofitServiceWrapperRetrofitServiceWrapper(baseUrl: String, clientKey: String, clientSecret: String) =
            RetrofitServiceWrapper(
                Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build(), clientKey, clientSecret
            )

        internal fun getVenueSearchDao(context: Context) = Room.databaseBuilder(context, VenueSearchDatabase::class.java, "RetrofitVenueSearchDB")
            .build()
            .locationSearchDao()
    }
}