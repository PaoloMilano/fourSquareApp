package com.magicbluepenguin.network.api

import com.magicbluepenguin.network.api.venuesearch.adapters.VenueDetailMoshiAdapter
import com.magicbluepenguin.network.api.venuesearch.adapters.VenueListItemMoshiAdapter
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object VenueSearchApiProvider {

    private val baseApiUrl = "https://api.foursquare.com/"
    private val clientKey = "ILPKWCRALTTMCDPKIGIJ0XB35QZORKI21A5EFWCV5EHJAFTR"
    private val clientSecret = "1M4K24DIQDK0HA3SG0N3HBQVHBJEG14UPN5OANYMWSN533NB"

    fun getWrapper(): VenueSearchApiWrapper {
        val moshi = Moshi.Builder()
            .add(VenueListItemMoshiAdapter)
            .add(VenueDetailMoshiAdapter)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseApiUrl).build()

        return RetrofitVenueSearchApiWrapper(retrofit, clientKey, clientSecret)
    }
}