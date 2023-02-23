package com.magicbluepenguin.features.venuesearch.di

import android.content.Context
import com.magicbluepenguin.repositories.venuesearch.VenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.getCachedVenueSearchRepository
import com.magicbluepenguin.utils.extensions.NetworkChangeReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object VenueSearchModule {

    @Provides
    @ViewModelScoped
    fun provideVenueSearchRepository(@ApplicationContext context: Context): VenueSearchRepository =
        getCachedVenueSearchRepository(context)

    @Provides
    @ViewModelScoped
    fun getNetworkChangeHandler(@ApplicationContext context: Context) = NetworkChangeReceiver(context)
}