package com.magicbluepenguin.repository.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem

@Dao
internal interface VenueSearchDao {

    @Transaction
    @Query("SELECT * FROM QueryModel WHERE `query` = :searchQuery")
    suspend fun getVenuesWithQuery(searchQuery: String): QueryWithLocations?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuery(queryModel: QueryModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenues(venueListItems: List<VenueListItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQueryWithVenue(queryWithLocation: QueryWithVenue)

    @Transaction
    suspend fun insertVenuesForQuery(query: String, venueListItems: List<VenueListItem>) {
        insertQuery(QueryModel(query))
        insertVenues(venueListItems)
        venueListItems.forEach {
            insertQueryWithVenue(QueryWithVenue(query, it.id))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenueDetails(venueDetail: VenueDetail)

    @Query("SELECT * FROM VenueDetail WHERE `id` = :venueId")
    suspend fun getVenueDetails(venueId: String): VenueDetail?

}