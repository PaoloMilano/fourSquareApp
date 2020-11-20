package com.magicbluepenguin.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.magicbluepenguin.repository.model.Venue

@Database(entities = [Venue::class, QueryModel::class, QueryWithVenue::class], version = 1)
abstract class VenueSearchDatabase : RoomDatabase() {
    internal abstract fun locationSearchDao(): VenueSearchDao
}