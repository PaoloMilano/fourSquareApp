package com.magicbluepenguin.foursquareapp.application

import com.magicbluepenguin.repository.repositories.LocationSearchRepository
import com.magicbluepenguin.repository.repositories.RetrofitLocationSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object ApiConfigModule {

    val baseApiUrl = "https://api.foursquare.com/"
    val clientKey = "ILPKWCRALTTMCDPKIGIJ0XB35QZORKI21A5EFWCV5EHJAFTR"
    val clientSecret = "1M4K24DIQDK0HA3SG0N3HBQVHBJEG14UPN5OANYMWSN533NB"

    @Provides
    fun provideSearchRepository(): LocationSearchRepository = RetrofitLocationSearchRepository(baseApiUrl, clientKey, clientSecret)
}