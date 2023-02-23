package com.magicblueopenguin.cache.venuesearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.magicbluepenguin.entities.venuesearch.VenueListItem

@Entity
internal data class VenueListItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String
) {

    fun toVenueListItem() = VenueListItem(
        id = id,
        name = name,
        address = address,
    )
}

internal fun VenueListItem.toVenueListItemEntity() = VenueListItemEntity(
    id = id,
    name = name,
    address = address,
)