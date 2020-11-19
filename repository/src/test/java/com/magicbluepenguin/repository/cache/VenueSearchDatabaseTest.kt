package com.magicbluepenguin.repository.cache

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.magicbluepenguin.repository.model.Venue
import com.magicbluepenguin.repository.model.VenueAddress
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VenueSearchDatabaseTest {

    val database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), VenueSearchDatabase::class.java, "VenueSearchDatabase").build()

    @Test
    fun `store and retrieve locations for queries`() = runBlocking {
        val expectedVenues = listOf(
            Venue("abc123", "Venue 1", VenueAddress(listOf("a", "b", "c"))),
            Venue("poi098", "Venue 2", VenueAddress(listOf("x", "y", "z"))),
            Venue("zxc345", "Venue 3", VenueAddress(listOf("1", "2", "3")))
        ).sortedBy { it.id }

        val expectedQuery = "Randomcity"

        database.locationSearchDao().insertVenuesForQuery(expectedQuery, expectedVenues)
        val venuesForQuery = database.locationSearchDao().getVenuesWithQuery(expectedQuery)

        Assert.assertEquals(expectedVenues, venuesForQuery.venues.sortedBy { it.id })
        Assert.assertEquals(expectedQuery, venuesForQuery.query.query)
    }

    @Test
    fun `venue update is reflected in retrieved values`() = runBlocking {
        val commonVenueId = "abc123"
        val initialVenue = Venue(commonVenueId, "Venue 1", VenueAddress(listOf("a", "b", "c")))
        val updateVenue = Venue(commonVenueId, "Venue 1", VenueAddress(listOf("x", "y", "z")))

        val expectedQuery = "Randomcity"
        database.locationSearchDao().insertVenuesForQuery(expectedQuery, listOf(initialVenue))

        database.locationSearchDao().insertVenues(listOf(updateVenue))

        val venuesForQuery = database.locationSearchDao().getVenuesWithQuery(expectedQuery)
        Assert.assertEquals(updateVenue, venuesForQuery.venues.first())
    }
}