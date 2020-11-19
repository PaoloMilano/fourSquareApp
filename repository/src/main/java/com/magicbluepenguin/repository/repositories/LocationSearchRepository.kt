package com.magicbluepenguin.repository.repositories

import com.magicbluepenguin.repository.api.RetrofitServiceWrapper
import com.magicbluepenguin.repository.api.location.LocationModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface LocationSearchRepository {
    suspend fun findVenuesNearLocation(location: String): List<LocationModel>
}

class RetrofitLocationSearchRepository(private val baseUrl: String, private val clientKey: String, private val clientSecret: String) :
    LocationSearchRepository {

    private val retrofitServiceWrapper by lazy {
        RetrofitServiceWrapper(
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(baseUrl)
                .build(), clientKey, clientSecret
        )
    }

    override suspend fun findVenuesNearLocation(location: String) =
        retrofitServiceWrapper.getLocationApiWrapper().listVenues(location).response.venues
}