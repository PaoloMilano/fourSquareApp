package com.magicbluepenguin.repository.api

import com.magicbluepenguin.repository.api.location.LocationApi
import com.magicbluepenguin.repository.api.location.LocationApiRetrofitWrapper
import retrofit2.Retrofit

internal class RetrofitServiceWrapper(private val retrofit: Retrofit, private val clientKey: String, private val clientSecret: String) {

    internal fun getLocationApiWrapper(): LocationApiRetrofitWrapper {
        return LocationApiRetrofitWrapper(
            clientKey,
            clientSecret,
            retrofit.create(LocationApi::class.java)
        )
    }
}