package com.magicbluepenguin.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.magicbluepenguin.repository.model.SizeablePhotos
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem

@Database(entities = [VenueListItem::class, QueryModel::class, QueryWithVenue::class, VenueDetail::class], version = 1)
@TypeConverters(SizeableImageConverter::class)
abstract class VenueSearchDatabase : RoomDatabase() {
    internal abstract fun locationSearchDao(): VenueSearchDao
}

class SizeableImageConverter {

    companion object {
        private const val SEPARATOR = "##"
    }

    @TypeConverter
    fun fromList(value: SizeablePhotos): String {
        return value.photosUrls.joinToString(separator = SEPARATOR)
    }

    @TypeConverter
    fun toSizeableImages(data: String) = SizeablePhotos(data.split(SEPARATOR))
}