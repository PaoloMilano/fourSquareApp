package com.magicbluepenguin.repository.api

import com.magicbluepenguin.repository.api.location.LocationApi
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
class RetrofitProviderTest {

    @Test
    fun `retrofit provider redirects calls with correct parameters`() = runBlockingTest {
        val clientKey = "fake_key"
        val clientSecret = "fake_secret"
        val location = "Amsterdam"

        val mockLocationApi = mockk<LocationApi>(relaxed = true)
        val mockRetrofit = mockk<Retrofit>(relaxed = true) {
            every { create(LocationApi::class.java) } answers { mockLocationApi }
        }

        RetrofitServiceWrapper(mockRetrofit, clientKey, clientSecret)
            .getLocationApiWrapper()
            .listVenues(location)

        coVerifySequence { mockLocationApi.listVenues(clientKey, clientSecret, location, 1000, 10, "20201118") }
    }
}