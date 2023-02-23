package com.magicblueopenguin.cache.venuesearch

import androidx.test.platform.app.InstrumentationRegistry
import com.magicbluepenguin.entities.venuesearch.SizablePhoto
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class VenueSearchDaoWrapperTest {

    private val venueSearchDaoWrapper = VenueSearchDaoWrapper(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test
    fun store_and_retrieve_venue_list_items_for_queries() = runBlocking {
        val expectedVenues = listOf(
            VenueListItem("abc123", "Venue 1", "a b c"),
            VenueListItem("poi098", "Venue 2", "x y z"),
            VenueListItem("zxc345", "Venue 3", "1 2 3")
        ).sortedBy { it.id }

        val expectedQuery = "Randomcity"

        venueSearchDaoWrapper.insertVenuesForQuery(expectedQuery, expectedVenues)
        val venuesForQuery = venueSearchDaoWrapper.getVenuesForQuery(expectedQuery)

        assertEquals(expectedVenues, venuesForQuery.sortedBy { it.id })
    }

    @Test
    fun venue_update_is_reflected_in_retrieved_values_list_items() = runBlocking {
        val commonVenueId = "abc123"
        val initialVenue = VenueListItem(commonVenueId, "Venue 1", "a b c")
        val updateVenue = VenueListItem(commonVenueId, "Venue 1", "x y z")

        val expectedQuery = "Randomcity"
        venueSearchDaoWrapper.insertVenuesForQuery(expectedQuery, listOf(initialVenue))
        venueSearchDaoWrapper.insertVenuesForQuery(expectedQuery, listOf(updateVenue))

        val venuesForQuery = venueSearchDaoWrapper.getVenuesForQuery(expectedQuery)
        assertEquals(updateVenue, venuesForQuery.first())
    }

    @Test
    fun store_and_retrieve_venue_details() = runBlocking {
        val venueId = "abc123"
        val expectedVenueDetails = VenueDetail(
            venueId,
            "Venue 1",
            "desc",
            listOf(SizablePhoto("prefix1", "suffix1"), SizablePhoto("prefix2", "suffix2")),
            "address 1",
            "06234567",
            3.2
        )
        venueSearchDaoWrapper.insertVenueDetails(expectedVenueDetails)
        assertEquals(expectedVenueDetails, venueSearchDaoWrapper.getVenueDetails(venueId))
    }

    @Test
    fun venue_details_are_updated_on_new_insert() = runBlocking {
        val venueId = "abc123"
        val venueDetails = VenueDetail(
            venueId,
            "Venue 1",
            "desc",
            listOf(SizablePhoto("prefix1", "suffix1"), SizablePhoto("prefix2", "suffix2")),
            "address 1",
            "06234567",
            3.2
        )
        val updatedVenueDetails = VenueDetail(
            venueId,
            "Venue 123",
            "desc",
            listOf(SizablePhoto("prefix1", "suffix1"), SizablePhoto("prefix2", "suffix2")),
            "address 3",
            "062397567",
            4.2
        )

        venueSearchDaoWrapper.insertVenueDetails(venueDetails)
        venueSearchDaoWrapper.insertVenueDetails(updatedVenueDetails)

        assertEquals(updatedVenueDetails, venueSearchDaoWrapper.getVenueDetails(venueId))
    }
}