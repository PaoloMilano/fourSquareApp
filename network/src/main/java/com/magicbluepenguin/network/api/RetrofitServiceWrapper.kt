package com.magicbluepenguin.network.api

import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.network.api.venuesearch.VenueSearchApi
import com.magicbluepenguin.network.api.venuesearch.adapters.VenueDetailMoshiAdapter
import com.magicbluepenguin.network.api.venuesearch.adapters.VenueListItemMoshiAdapter
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitVenueSearchApiWrapper(baseUrl: String, private val clientId: String, private val clientSecret: String) {

    val moshi = Moshi.Builder()
        .add(VenueListItemMoshiAdapter)
        .add(VenueDetailMoshiAdapter)
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl).build()

    private val venueSearchApi = retrofit.create(VenueSearchApi::class.java)

    suspend fun listVenues(
        location: String,
        radius: Int = 1000,
        limit: Int = 100,
        version: String = API_VERSION
    ) = venueSearchApi.listVenues(clientId, clientSecret, location, radius, limit, version)

    suspend fun getVenueDetails(venueId: String): VenueDetail? = venueSearchApi.venueDetails(venueId, clientId, clientSecret, API_VERSION)

}

private const val API_VERSION = "20201118"
