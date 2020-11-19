package com.magicbluepenguin.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

internal data class VenueSearchApiResponse(val response: VenueListResponse)

internal data class VenueListResponse(val venues: List<Venue>)

@Entity
data class Venue(
    @PrimaryKey
    val id: String,
    val name: String,
    val location: VenueAddress
)

data class VenueAddress(val formattedAddress: List<String>)