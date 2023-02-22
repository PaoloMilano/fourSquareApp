package com.magicblueopenguin.cache.venuesearch.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.magicblueopenguin.cache.venuesearch.model.VenueDetailEntity
import com.magicblueopenguin.cache.venuesearch.model.VenueListItemEntity

@Dao
interface VenueSearchDao {

    @Transaction
    @Query("SELECT * FROM QueryModel WHERE `query` = :searchQuery")
    suspend fun getVenuesForQuery(searchQuery: String): QueryWithLocations?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuery(queryModel: QueryModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenues(venueListItems: List<VenueListItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQueryWithVenue(queryWithLocation: QueryWithVenue)

    @Transaction
    suspend fun insertVenuesForQuery(query: String, venueListItems: List<VenueListItemEntity>) {
        insertQuery(QueryModel(query))
        insertVenues(venueListItems)
        venueListItems.forEach {
            insertQueryWithVenue(QueryWithVenue(query, it.id))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenueDetails(venueDetail: VenueDetailEntity)

    @Query("SELECT * FROM VenueDetailEntity WHERE `id` = :venueId")
    suspend fun getVenueDetails(venueId: String): VenueDetailEntity?

}