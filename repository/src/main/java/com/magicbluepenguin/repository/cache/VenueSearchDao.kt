package com.magicbluepenguin.repository.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.magicbluepenguin.repository.model.Venue

@Dao
internal interface VenueSearchDao {

    @Transaction
    @Query("SELECT * FROM QueryModel WHERE `query` = :searchQuery")
    suspend fun getVenuesWithQuery(searchQuery: String): QueryWithLocations

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuery(queryModel: QueryModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenues(venues: List<Venue>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQueryWithVenue(queryWithLocation: QueryWithVenue)

    @Transaction
    suspend fun insertVenuesForQuery(query: String, venues: List<Venue>) {
        insertQuery(QueryModel(query))
        insertVenues(venues)
        venues.forEach {
            insertQueryWithVenue(QueryWithVenue(query, it.id))
        }
    }
}