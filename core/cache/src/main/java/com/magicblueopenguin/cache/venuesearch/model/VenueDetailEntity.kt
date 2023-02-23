package com.magicblueopenguin.cache.venuesearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.magicbluepenguin.entities.venuesearch.SizablePhoto
import com.magicbluepenguin.entities.venuesearch.VenueDetail

@Entity
internal data class VenueDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val photos: List<SizablePhoto>,
    val formattedPhoneNumber: String,
    val address: String,
    val rating: Double
) {
    fun toVenueDetail() = VenueDetail(
        id = id,
        name = name,
        description = description,
        photos = photos,
        formattedPhoneNumber = formattedPhoneNumber,
        address = address,
        rating = rating
    )
}

internal fun VenueDetail.toVenueDetailEntity() = VenueDetailEntity(
    id = id,
    name = name,
    description = description,
    photos = photos,
    formattedPhoneNumber = formattedPhoneNumber,
    address = address,
    rating = rating
)
