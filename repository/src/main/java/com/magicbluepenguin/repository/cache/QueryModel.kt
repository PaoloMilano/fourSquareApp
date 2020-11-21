package com.magicbluepenguin.repository.cache

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.magicbluepenguin.repository.model.VenueListItem

@Entity
data class QueryModel(@PrimaryKey val query: String)

@Entity(primaryKeys = ["query", "venueId"])
class QueryWithVenue(
    @ColumnInfo(index = true)
    val query: String,
    @ColumnInfo(index = true)
    val venueId: String
)

@Entity
data class QueryWithLocations(
    @Embedded
    var query: QueryModel,
    @Relation(
        parentColumn = "query",
        entity = VenueListItem::class,
        entityColumn = "id",
        associateBy = Junction(
            value = QueryWithVenue::class,
            parentColumn = "query",
            entityColumn = "venueId"
        )
    )
    var venueListItems: List<VenueListItem>
)