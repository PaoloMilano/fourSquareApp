package com.magicbluepenguin.repository.api.venuesearch.adapters

import com.magicbluepenguin.repository.model.VenueListItem
import com.squareup.moshi.FromJson

internal object VenueListItemMoshiAdapter {
    @FromJson
    fun fromJson(responseHolder: ResponseHolder): List<VenueListItem> {
        return responseHolder.response.toVenuesObjectList()?.toVenues() ?: emptyList()
    }
}

private fun Map<String, Any?>.toVenuesObjectList() = this["venues"] as? List<Map<String, Any?>>

private fun List<Map<String, Any?>>.toVenues() = map {
    fun Map<String, Any?>.toLocation() = this["location"] as? Map<String, Any?> ?: emptyMap()
    fun Map<String, Any?>.toFormattedAddress() = (this["formattedAddress"] as? List<String>)?.joinToString(separator = "\n") ?: ""

    val id = it["id"] as? String ?: return@map null
    val name = it["name"] as? String ?: ""
    val address = it.toLocation().toFormattedAddress()
    VenueListItem(id, name, address)
}.filterNotNull()

