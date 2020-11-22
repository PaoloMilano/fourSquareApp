package com.magicbluepenguin.foursquareapp.application

import android.content.Context
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import com.magicbluepenguin.repository.repositories.getCachedVenueSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object ApiConfigModule {

    val baseApiUrl = "https://api.foursquare.com/"
    val clientKey = "ILPKWCRALTTMCDPKIGIJ0XB35QZORKI21A5EFWCV5EHJAFTR"
    val clientSecret = "1M4K24DIQDK0HA3SG0N3HBQVHBJEG14UPN5OANYMWSN533NB"

    @Provides
    fun provideVenueSearchRepository(@ApplicationContext context: Context): VenueSearchRepository =
        getCachedVenueSearchRepository(context, baseApiUrl, clientKey, clientSecret)
}