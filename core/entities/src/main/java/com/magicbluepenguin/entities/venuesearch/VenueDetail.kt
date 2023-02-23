package com.magicbluepenguin.entities.venuesearch

import androidx.annotation.Keep

@Keep
data class VenueDetail(
    val id: String,
    val name: String,
    val description: String,
    val photos: List<SizablePhoto>,
    val formattedPhoneNumber: String,
    val address: String,
    val rating: Double
)