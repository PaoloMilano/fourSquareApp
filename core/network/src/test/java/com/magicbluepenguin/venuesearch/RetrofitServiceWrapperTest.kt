package com.magicbluepenguin.venuesearch

import com.magicbluepenguin.network.api.RetrofitVenueSearchApiWrapper
import com.magicbluepenguin.network.api.venuesearch.VenueSearchApi
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
class RetrofitServiceWrapperTest {

    @Test
    fun `retrofit provider redirects calls with correct parameters`() = runTest {
        val clientKey = "fake_key"
        val clientSecret = "fake_secret"
        val location = "Amsterdam"

        val mockLocationApi = mockk<VenueSearchApi>(relaxed = true) {
            coEvery { listVenues(any(), any(), any(), any(), any(), any()) } answers { emptyList() }
        }
        val mockRetrofit = mockk<Retrofit>(relaxed = true) {
            every { create(VenueSearchApi::class.java) } answers { mockLocationApi }
        }

        RetrofitVenueSearchApiWrapper(mockRetrofit, clientKey, clientSecret)
            .listVenues(location)

        coVerifySequence { mockLocationApi.listVenues(clientKey, clientSecret, location, 1000, 100, "20201118") }
    }
}