package com.magicbluepenguin.repository.api.venuesearch.adapters

import com.magicbluepenguin.repository.model.SizeablePhotos
import com.magicbluepenguin.repository.model.VenueDetail
import com.squareup.moshi.FromJson

internal object VenueDetailMoshiAdapter {
    @FromJson
    fun fromJson(responseHolder: ResponseHolder): VenueDetail? {
        return responseHolder.response.toVenue()?.toVenueDetails()
    }
}

fun Map<String, Any?>.toVenue() = this["venue"] as? Map<String, Any?>

fun Map<String, Any?>.toVenueDetails(): VenueDetail {
    fun Map<String, Any?>.toFormattedPhoneNumber() = (this["contact"] as? Map<String, String>)?.get("formattedPhone") ?: ""
    fun Map<String, Any?>.toLocation() = this["location"] as? Map<String, Any?> ?: emptyMap()
    fun Map<String, Any?>.toFormattedAddress() = (this["formattedAddress"] as? List<String> ?: emptyList()).joinToString(separator = "\n")
    fun Map<String, Any?>.toRating() = (this["rating"] as? Map<String, Float>)?.get("rating") ?: 0F

    val venueId = this["id"]?.toString() ?: ""
    val venueName = this["name"]?.toString() ?: ""
    val description = this["description"]?.toString() ?: ""
    val address = toLocation().toFormattedAddress()
    val phoneNumber = toFormattedPhoneNumber()
    val rating = toRating()
    val photos = toSizeablePhotos()
    return VenueDetail(venueId, venueName, description, SizeablePhotos(photos), phoneNumber, address, rating)
}

private fun Map<String, Any?>.toSizeablePhotos(): List<String> {
    fun Map<String, Any?>.toPhotosObject() = this["photos"] as? Map<String, Any?>
    fun Map<String, Any?>.toGroupsObject() = this["groups"] as? List<Map<String, Any?>>
    fun List<Map<String, Any?>>.toItemsObject() = map { it["items"] as? List<Map<String, Any?>> }
    fun List<Map<String, Any?>>.toPhotoUrls() = map { "${it["prefix"]}%dx%d${it["suffix"]}" }
    return toPhotosObject()?.toGroupsObject()?.toItemsObject()?.flatMap { it?.toPhotoUrls() ?: emptyList() } ?: emptyList()
}

