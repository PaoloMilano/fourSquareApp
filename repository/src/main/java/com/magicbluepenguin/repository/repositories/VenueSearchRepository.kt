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
    val moshi = Moshi.Builder()
        .add(VenueListItemMoshiAdapter)
        .add(VenueDetailMoshiAdapter)
        .build()

    val retrofitWrapper = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl).build().run {
            RetrofitVenueSearchApiWrapper(this, clientKey, clientSecret)
        }

    val venueSearchDao = Room.databaseBuilder(context, VenueSearchDatabase::class.java, "RetrofitVenueSearchDB")
        .build()
        .locationSearchDao()
    return RetrofitVenueSearchRepository(venueSearchDao, retrofitWrapper)
}