package com.magicblueopenguin.cache.venuesearch

import android.content.Context
import androidx.room.Room
import com.magicblueopenguin.cache.venuesearch.datasource.VenueSearchDatabase
import com.magicblueopenguin.cache.venuesearch.model.toVenueDetailEntity
import com.magicblueopenguin.cache.venuesearch.model.toVenueListItemEntity
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem

class VenueSearchDaoWrapper(context: Context) {

    private val venueSearchDao = Room.databaseBuilder(context, VenueSearchDatabase::class.java, "RetrofitVenueSearchDB")
        .build().locationSearchDao()

    suspend fun getVenuesForQuery(searchQuery: String): List<VenueListItem> {
        return venueSearchDao.getVenuesForQuery(searchQuery)?.venueListItems?.map { it.toVenueListItem() } ?: emptyList()
    }

    suspend fun insertVenuesForQuery(query: String, venueListItems: List<VenueListItem>) {
        venueSearchDao.insertVenuesForQuery(query, venueListItems.map { it.toVenueListItemEntity() })
    }

    suspend fun insertVenueDetails(venueDetail: VenueDetail) {
        venueSearchDao.insertVenueDetails(venueDetail.toVenueDetailEntity())
    }

    suspend fun getVenueDetails(venueId: String): VenueDetail? {
        return venueSearchDao.getVenueDetails(venueId)?.toVenueDetail()
    }
}