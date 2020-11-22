package com.magicbluepenguin.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.magicbluepenguin.repository.model.SizablePhoto
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem

@Database(entities = [VenueListItem::class, QueryModel::class, QueryWithVenue::class, VenueDetail::class], version = 1)
@TypeConverters(SizeableImageConverter::class)
abstract class VenueSearchDatabase : RoomDatabase() {
    internal abstract fun locationSearchDao(): VenueSearchDao
}

class SizeableImageConverter {

    companion object {
        private const val COMPONENTS_SEPARATOR = "#_#"
        private const val ENTITY_SEPARATOR = "#_#_#"
    }

    @TypeConverter
    fun fromList(value: List<SizablePhoto>): String {
        return value.map { "${it.photoPrefix}$COMPONENTS_SEPARATOR${it.photoSuffix}" }.joinToString(separator = ENTITY_SEPARATOR)
    }

    @TypeConverter
    fun toSizeableImages(data: String) = data.split(ENTITY_SEPARATOR).map { it.split(COMPONENTS_SEPARATOR) }.map { SizablePhoto(it[0], it[1]) }
}