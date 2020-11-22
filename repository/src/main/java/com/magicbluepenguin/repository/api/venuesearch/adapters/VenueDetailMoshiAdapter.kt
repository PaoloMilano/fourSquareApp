package com.magicbluepenguin.repository.api.venuesearch.adapters

import com.magicbluepenguin.repository.model.SizablePhoto
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

    val venueId = this["id"]?.toString() ?: ""
    val venueName = this["name"]?.toString() ?: ""
    val description = this["description"]?.toString() ?: ""
    val rating = this["rating"] as? Double ?: 0.0
    val address = toLocation().toFormattedAddress()
    val phoneNumber = toFormattedPhoneNumber()
    val photos = toSizablePhotos()
    return VenueDetail(venueId, venueName, description, photos, phoneNumber, address, rating)
}

private fun Map<String, Any?>.toSizablePhotos(): List<SizablePhoto> {
    fun Map<String, Any?>.toPhotosObject() = this["photos"] as? Map<String, Any?>
    fun Map<String, Any?>.toGroupsObject() = this["groups"] as? List<Map<String, Any?>>
    fun List<Map<String, Any?>>.toItemsObject() = map { it["items"] as? List<Map<String, Any?>> }
    fun List<Map<String, Any?>>.toPhotoUrls() = map { it["prefix"].toString() to it["suffix"].toString() }
    val urlList = toPhotosObject()?.toGroupsObject()?.toItemsObject()?.flatMap { it?.toPhotoUrls() ?: emptyList() } ?: emptyList()
    return urlList.map { SizablePhoto(it.first, it.second) }
}

