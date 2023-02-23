package com.magicbluepenguin.venuesearch.adapters

import com.magicbluepenguin.network.api.venuesearch.adapters.ResponseHolder
import com.magicbluepenguin.network.api.venuesearch.adapters.VenueListItemMoshiAdapter
import org.junit.Assert.assertEquals
import org.junit.Test

internal class VenueListItemMoshiAdapterTest {

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
        val venue = VenueListItemMoshiAdapter.fromJson(ResponseHolder(venueResponseMap))

        assertEquals(1, venue.size)
        assertEquals(id, venue[0].id)
        assertEquals(vanueName, venue[0].name)
        assertEquals(formmatedAddressObject.joinToString(separator = "\n"), venue[0].address)
    }
}