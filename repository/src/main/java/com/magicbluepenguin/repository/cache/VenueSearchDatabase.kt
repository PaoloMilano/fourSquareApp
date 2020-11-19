package com.magicbluepenguin.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.magicbluepenguin.repository.model.Venue
import com.magicbluepenguin.repository.model.VenueAddress

@Database(entities = [Venue::class, QueryModel::class, QueryWithVenue::class], version = 1)
@TypeConverters(VenueAddressConverter::class)
abstract class VenueSearchDatabase : RoomDatabase() {
    internal abstract fun locationSearchDao(): VenueSearchDao
}

internal class VenueAddressConverter {
    @TypeConverter
    fun toFormattedAddress(value: String): VenueAddress {
        return VenueAddress(value.split("%").toList())
    }

    @TypeConverter
    fun toString(location: VenueAddress): String {
        return location.formattedAddress.joinToString("%")
    }
}