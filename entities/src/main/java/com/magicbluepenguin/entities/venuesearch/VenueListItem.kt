package com.magicbluepenguin.entities.venuesearch

import androidx.annotation.Keep

@Keep
data class VenueListItem(
    val id: String,
    val name: String,
    val address: String
)