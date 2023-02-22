package com.magicblueopenguin.cache.venuesearch.datasource

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.magicblueopenguin.cache.venuesearch.model.VenueListItemEntity

@Keep
@Entity
data class QueryModel(@PrimaryKey val query: String)

@Entity(primaryKeys = ["query", "venueId"])
class QueryWithVenue(
    @ColumnInfo(index = true)
    val query: String,
    @ColumnInfo(index = true)
    val venueId: String
)

@Keep
@Entity
data class QueryWithLocations(
    @Embedded
    var query: QueryModel,
    @Relation(
        parentColumn = "query",
        entity = VenueListItemEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = QueryWithVenue::class,
            parentColumn = "query",
            entityColumn = "venueId"
        )
    )
    var venueListItems: List<VenueListItemEntity>
)