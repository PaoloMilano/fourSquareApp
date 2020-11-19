package com.magicbluepenguin.repository.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.magicbluepenguin.repository.model.Venue

@Entity
data class QueryModel(@PrimaryKey val query: String)

@Entity(primaryKeys = ["query", "venueId"])
class QueryWithVenue(
    val query: String,
    val venueId: String
)

data class QueryWithLocations(
    @Embedded
    var query: QueryModel,
    @Relation(
        parentColumn = "query",
        entity = Venue::class,
        entityColumn = "id",
        associateBy = Junction(
            value = QueryWithVenue::class,
            parentColumn = "query",
            entityColumn = "venueId"
        )
    )
    var venues: List<Venue>
)