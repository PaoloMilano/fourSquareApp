package com.magicbluepenguin.repository.api.venuesearch

import junit.framework.Assert.assertEquals
import org.junit.Test

internal class VenueMoshiAdapterTest {

    @Test
    fun parse_venue_list_response() {

        val id = "location_id"
        val vanueName = "venue_name"
        val formmatedAddressObject = listOf("Addr Line1", "Addr line2")

        val locationObject = mapOf("formattedAddress" to formmatedAddressObject)
        val venueObject = mapOf(
            "id" to id,
            "name" to vanueName,
            "location" to locationObject
        )
        val venueResponseMap = mapOf("venues" to listOf(venueObject))
        val venue = VenueMoshiAdapter().fromJson(ResponseHolder(venueResponseMap))

        assertEquals(1, venue.size)
        assertEquals(id, venue[0].id)
        assertEquals(vanueName, venue[0].name)
        assertEquals(formmatedAddressObject.joinToString(separator = "\n"), venue[0].location)
    }
}