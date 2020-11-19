package com.magicbluepenguin.repository.api

import com.magicbluepenguin.repository.api.venuesearch.VenueSearchApi
import com.magicbluepenguin.repository.api.venuesearch.VenueSearchApiWrapper
import retrofit2.Retrofit

internal class RetrofitServiceWrapper(private val retrofit: Retrofit, private val clientKey: String, private val clientSecret: String) {

    internal fun getVenueSearchApiWrapper(): VenueSearchApiWrapper {
        return VenueSearchApiWrapper(
            clientKey,
            clientSecret,
            retrofit.create(VenueSearchApi::class.java)
        )
    }
}