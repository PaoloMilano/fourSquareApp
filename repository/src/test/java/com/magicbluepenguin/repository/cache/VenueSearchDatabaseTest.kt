package com.magicbluepenguin.repository.cache

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.magicblueopenguin.cache.cache.venuesearch.datasource.VenueSearchDatabase
import com.magicbluepenguin.repository.model.SizablePhoto
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VenueSearchDatabaseTest {

    val database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), VenueSearchDatabase::class.java, "VenueSearchDatabase").build()

    @Test
    fun `store and retrieve venue list items for queries`() = runBlocking {
        val expectedVenues = listOf(
            VenueListItem("abc123", "Venue 1", "a b c"),
            VenueListItem("poi098", "Venue 2", "x y z"),
            VenueListItem("zxc345", "Venue 3", "1 2 3")
        ).sortedBy { it.id }

        val expectedQuery = "Randomcity"

        database.locationSearchDao().insertVenuesForQuery(expectedQuery, expectedVenues)
        val venuesForQuery = database.locationSearchDao().getVenuesWithQuery(expectedQuery)

        assertEquals(expectedVenues, venuesForQuery?.venueListItems?.sortedBy { it.id })
        assertEquals(expectedQuery, venuesForQuery?.query?.query)
    }

    @Test
    fun `venue update is reflected in retrieved values list items`() = runBlocking {
        val commonVenueId = "abc123"
        val initialVenue = VenueListItem(commonVenueId, "Venue 1", "a b c")
        val updateVenue = VenueListItem(commonVenueId, "Venue 1", "x y z")

        val expectedQuery = "Randomcity"
        database.locationSearchDao().insertVenuesForQuery(expectedQuery, listOf(initialVenue))

        database.locationSearchDao().insertVenues(listOf(updateVenue))

        val venuesForQuery = database.locationSearchDao().getVenuesWithQuery(expectedQuery)
        Assert.assertEquals(updateVenue, venuesForQuery?.venueListItems?.first())
    }

    @Test
    fun `store and retrieve venue details`() = runBlocking {
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
        database.locationSearchDao().insertVenueDetails(expectedVenueDetails)

        val retrievedVenueDetails = database.locationSearchDao().getVenueDetails(venueId)
        assertEquals(expectedVenueDetails, retrievedVenueDetails)
    }

    @Test
    fun `venue details are updated on new insert`() = runBlocking {
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

        database.locationSearchDao().insertVenueDetails(venueDetails)
        database.locationSearchDao().insertVenueDetails(updatedVenueDetails)

        val retrievedVenueDetails = database.locationSearchDao().getVenueDetails(venueId)
        assertEquals(updatedVenueDetails, retrievedVenueDetails)
    }
}