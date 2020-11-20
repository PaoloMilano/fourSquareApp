package com.magicbluepenguin.repository.api.venuesearch

import com.magicbluepenguin.repository.model.Venue
import com.squareup.moshi.FromJson

internal data class ResponseHolder(val response: Map<String, Any?>)

internal class VenueMoshiAdapter {
    @FromJson
    fun fromJson(responseHolder: ResponseHolder): List<Venue> {
        return responseHolder.response.toVenuesObjectList().toVenues()
    }
}

fun Map<String, Any?>.toVenuesObjectList() = this["venues"] as? List<Map<String, Any?>> ?: emptyList()

fun List<Map<String, Any?>>.toVenues() = map {
    val id = it["id"] as? String ?: return@map null
    val name = it["name"] as? String ?: ""
    val address = it.toLocation().toFormattedAddress().joinToString(separator = "\n")
    Venue(id, name, address)
}.filterNotNull()

fun Map<String, Any?>.toLocation() = this["location"] as? Map<String, Any?> ?: emptyMap()

fun Map<String, Any?>.toFormattedAddress() = this["formattedAddress"] as? List<String> ?: emptyList()