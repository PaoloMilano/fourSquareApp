package com.magicbluepenguin.repository.repositories

import androidx.test.core.app.ApplicationProvider
import com.magicbluepenguin.repository.api.RetrofitServiceWrapper
import com.magicbluepenguin.repository.api.venuesearch.VenueSearchApiWrapper
import com.magicbluepenguin.repository.cache.VenueSearchDao
import com.magicbluepenguin.repository.model.VenueListItem
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RetrofitVenueSearchRepositoryTest {

    private val mockVenueSearchDao = mockk<VenueSearchDao>(relaxed = true)
    private val mockSearchApiWrapper = mockk<VenueSearchApiWrapper>()

    private lateinit var venueSearchRepository: RetrofitVenueSearchRepository

    @Before
    fun setUp() {
        mockkObject(RetrofitVenueSearchRepository.Companion)

        val mockRetrofitServiceWrapper = mockk<RetrofitServiceWrapper>()
        every { mockRetrofitServiceWrapper.getVenueSearchApiWrapper() } answers { mockSearchApiWrapper }
        every {
            RetrofitVenueSearchRepository.Companion.getRetrofitServiceWrapperRetrofitServiceWrapper(any(), any(), any())
        } answers { mockRetrofitServiceWrapper }
        every { RetrofitVenueSearchRepository.Companion.getVenueSearchDao(any()) } answers { mockVenueSearchDao }
        coEvery { mockVenueSearchDao.getVenuesWithQuery(any()) } answers {
            mockk { every { venueListItems } answers { emptyList() } }
        }

        venueSearchRepository = RetrofitVenueSearchRepository(ApplicationProvider.getApplicationContext(), "http://base.nl", "123", "abc")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `venue items are cached before being returned`() = runBlockingTest {
        val venuesList = listOf<VenueListItem>(mockk(), mockk(), mockk())
        coEvery { mockSearchApiWrapper.listVenues(any()) } answers { venuesList }

        val queryLocatiopn = "Amsterdam"
        venueSearchRepository.findVenuesNearLocation(queryLocatiopn)

        coVerifySequence {
            mockVenueSearchDao.insertVenuesForQuery(queryLocatiopn, venuesList)
            mockVenueSearchDao.getVenuesWithQuery(queryLocatiopn)
        }
    }
}