package com.magicbluepenguin.repository.api.venuesearch.adapters

import com.magicbluepenguin.repository.model.SizablePhotos
import com.magicbluepenguin.repository.model.VenueDetail
import org.junit.Assert.assertEquals
import org.junit.Test

internal class VenueDetailMoshiAdapterTest {

    @Test
    fun parse_venue_list_response() {
        val venueId = "venue id"
        val venueName = "Venue name"
        val description = "description"
        val photoPrefix = "prefix"
        val photoSuffix = "suffix"
        val venueAddress = listOf("Line1", "Line2", "Line3")
        val phoneNumber = "0612345678"
        val rating = 8.3

        val photoList = listOf(mapOf("prefix" to photoPrefix, "suffix" to photoSuffix))
        val photoItemsList = listOf(mapOf("items" to photoList))
        val groups = mapOf("groups" to photoItemsList)
        val contactObject = mapOf("formattedPhone" to phoneNumber)
        val locationObject = mapOf("formattedAddress" to venueAddress)

        val venueMap = mapOf(
            "id" to venueId,
            "name" to venueName,
            "description" to description,
            "rating" to rating,
            "photos" to groups,
            "contact" to contactObject,
            "location" to locationObject
        )
        val venueResponseMap = mapOf("venue" to venueMap)

        val expectedResult = VenueDetail(
            venueId,
            venueName,
            description,
            SizablePhotos(listOf("prefix%dx%dsuffix")),
            phoneNumber,
            venueAddress.joinToString(separator = "\n"),
            rating
        )
        assertEquals(expectedResult, VenueDetailMoshiAdapter.fromJson(ResponseHolder(venueResponseMap)))
    }
}

