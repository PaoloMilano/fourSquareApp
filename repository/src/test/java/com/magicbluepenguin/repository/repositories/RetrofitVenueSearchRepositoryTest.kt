package com.magicbluepenguin.repository.repositories

import com.magicbluepenguin.repository.api.RetrofitVenueSearchApiWrapper
import com.magicblueopenguin.cache.cache.venuesearch.datasource.VenueSearchDao
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RetrofitVenueSearchRepositoryTest {

    private val mockVenueSearchDao = mockk<VenueSearchDao>(relaxed = true)
    private val mockSearchApiWrapper = mockk<RetrofitVenueSearchApiWrapper>()

    private lateinit var venueSearchRepository: RetrofitVenueSearchRepository

    @Before
    fun setUp() {
        coEvery { mockVenueSearchDao.getVenuesWithQuery(any()) } answers {
            mockk { every { venueListItems } answers { emptyList() } }
        }

        venueSearchRepository = RetrofitVenueSearchRepository(mockVenueSearchDao, mockSearchApiWrapper)
    }

    @Test
    fun `venue items are cached before being returned`() = runBlockingTest {
        val queryLocatiopn = "Amsterdam"

        val mockVenuesList = mockk<List<VenueListItem>>()
        coEvery { mockSearchApiWrapper.listVenues(queryLocatiopn) } answers { mockVenuesList }

        venueSearchRepository.findVenuesNearLocation(queryLocatiopn)

        coVerifySequence {
            mockVenueSearchDao.insertVenuesForQuery(queryLocatiopn, mockVenuesList)
            mockVenueSearchDao.getVenuesWithQuery(queryLocatiopn)
        }
    }

    @Test
    fun `venue detail object is cached before being returned`() = runBlockingTest {
        val venueId = "random_id"

        val mockVenuesDetail = mockk<VenueDetail>()
        coEvery { mockSearchApiWrapper.getVenueDetails(venueId) } answers { mockVenuesDetail }

        venueSearchRepository.getVenueDetails(venueId)

        coVerifySequence {
            mockVenueSearchDao.insertVenueDetails(mockVenuesDetail)
            mockVenueSearchDao.getVenueDetails(venueId)
        }
    }

    @Test
    fun `error list response is returned from cache in case of exception`() = runBlockingTest {
        val queryLocatiopn = "Amsterdam"

        val mockVenuesList = mockk<List<VenueListItem>>()
        coEvery { mockVenueSearchDao.getVenuesWithQuery(queryLocatiopn) } answers {
            mockk {
                every { venueListItems } answers { mockVenuesList }
            }
        }
        coEvery { mockSearchApiWrapper.listVenues(queryLocatiopn) } throws Exception()

        val result = venueSearchRepository.findVenuesNearLocation(queryLocatiopn)

        if (result is ErrorResponse) {
            assertEquals(mockVenuesList, result.data)
        } else {
            fail("Error not returned from failed request")
        }
    }

    @Test
    fun `error detail response is returned from cache in case of exception`() = runBlockingTest {
        val venueId = "random_id"

        val mockVenuesDetail = mockk<VenueDetail>()
        coEvery { mockVenueSearchDao.getVenueDetails(venueId) } answers { mockVenuesDetail }
        coEvery { mockSearchApiWrapper.getVenueDetails(venueId) } throws Exception()

        val result = venueSearchRepository.getVenueDetails(venueId)

        if (result is ErrorResponse) {
            assertEquals(mockVenuesDetail, result.data)
        } else {
            fail("Error not returned from failed request")
        }
    }
}