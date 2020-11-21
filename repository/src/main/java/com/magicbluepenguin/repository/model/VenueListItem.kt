package com.magicbluepenguin.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VenueListItem(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String
)
