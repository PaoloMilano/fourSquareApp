package com.magicbluepenguin.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VenueDetail(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val photos: SizeablePhotos,
    val formattedPhoneNumber: String,
    val address: String,
    val rating: Float
)