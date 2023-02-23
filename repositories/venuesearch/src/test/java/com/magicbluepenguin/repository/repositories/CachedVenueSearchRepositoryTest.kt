package com.magicbluepenguin.repository.repositories

import com.magicblueopenguin.cache.venuesearch.VenueSearchDaoWrapper
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.network.api.VenueSearchApiWrapper
import com.magicbluepenguin.repositories.venuesearch.CachedVenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.response.NetworkError
import com.magicbluepenguin.repositories.venuesearch.response.ServerError
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CachedVenueSearchRepositoryTest {

    private val mockIsNetworkAvailable = mockk<() -> Boolean>()
    private val mockVenueSearchDao = mockk<VenueSearchDaoWrapper>(relaxed = true)
    private val mockSearchApiWrapper = mockk<VenueSearchApiWrapper>()
    private val venueSearchRepository =
        CachedVenueSearchRepository(mockVenueSearchDao, mockSearchApiWrapper, isNetworkAvailable = mockIsNetworkAvailable)

    @Before
    fun setUp() {
        coEvery { mockVenueSearchDao.getVenuesForQuery(any()) } answers { emptyList() }
    }

    @Test
    fun `venue items are cached before being returned`() = runTest {
        val queryLocatiopn = "Amsterdam"

        val mockVenuesList = mockk<List<VenueListItem>>()
        coEvery { mockSearchApiWrapper.listVenues(queryLocatiopn) } answers { mockVenuesList }

        venueSearchRepository.findVenuesNearLocation(queryLocatiopn)

        coVerifySequence {
            mockVenueSearchDao.insertVenuesForQuery(queryLocatiopn, mockVenuesList)
            mockVenueSearchDao.getVenuesForQuery(queryLocatiopn)
        }
    }

    @Test
    fun `venue detail object is cached before being returned`() = runTest {
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
    fun `error list response is returned from cache in case of exception`() = runTest {
        val queryLocatiopn = "Amsterdam"

        val mockVenuesList = mockk<List<VenueListItem>>()
        every { mockIsNetworkAvailable.invoke() } answers { true }
        coEvery { mockVenueSearchDao.getVenuesForQuery(queryLocatiopn) } answers { mockVenuesList }
        coEvery { mockSearchApiWrapper.listVenues(queryLocatiopn) } throws Exception()

        val result = venueSearchRepository.findVenuesNearLocation(queryLocatiopn)

        if (result.error != null) {
            assertEquals(mockVenuesList, result.data)
        } else {
            fail("Error not returned from failed request")
        }
    }

    @Test
    fun `error detail response is returned from cache in case of exception`() = runTest {
        val venueId = "random_id"

        val mockVenuesDetail = mockk<VenueDetail>()
        every { mockIsNetworkAvailable.invoke() } answers { true }
        coEvery { mockVenueSearchDao.getVenueDetails(venueId) } answers { mockVenuesDetail }
        coEvery { mockSearchApiWrapper.getVenueDetails(venueId) } throws Exception()

        val result = venueSearchRepository.getVenueDetails(venueId)

        if (result.error != null) {
            assertEquals(mockVenuesDetail, result.data)
        } else {
            fail("Error not returned from failed request")
        }
    }
    @Test
    fun `on failure repository returns a network error when network is not available`() = runTest {
        every { mockIsNetworkAvailable.invoke() } answers { false }
        coEvery { mockSearchApiWrapper.getVenueDetails(any()) } throws Exception()
        coEvery { mockSearchApiWrapper.listVenues(any()) } throws Exception()

        val firstResult = venueSearchRepository.findVenuesNearLocation("")
        val secondResult = venueSearchRepository.getVenueDetails("")

        assertEquals(NetworkError, firstResult.error)
        assertEquals(NetworkError, secondResult.error)
    }

    @Test
    fun `on failure repository returns a server error when network is available`() = runTest {
        every { mockIsNetworkAvailable.invoke() } answers { true }
        coEvery { mockSearchApiWrapper.getVenueDetails(any()) } throws Exception()
        coEvery { mockSearchApiWrapper.listVenues(any()) } throws Exception()

        val firstResult = venueSearchRepository.findVenuesNearLocation("")
        val secondResult = venueSearchRepository.getVenueDetails("")

        assertEquals(ServerError, firstResult.error)
        assertEquals(ServerError, secondResult.error)
    }
}