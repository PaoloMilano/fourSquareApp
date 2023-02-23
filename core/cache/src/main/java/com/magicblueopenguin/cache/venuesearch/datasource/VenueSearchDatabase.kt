package com.magicblueopenguin.cache.venuesearch.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.magicblueopenguin.cache.venuesearch.model.VenueDetailEntity
import com.magicblueopenguin.cache.venuesearch.model.VenueListItemEntity
import com.magicbluepenguin.entities.venuesearch.SizablePhoto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Database(entities = [VenueDetailEntity::class, VenueListItemEntity::class, QueryModel::class, QueryWithVenue::class], version = 1)
@TypeConverters(SizeableImageConverter::class)
internal abstract class VenueSearchDatabase : RoomDatabase() {
    abstract fun locationSearchDao(): VenueSearchDao
}

internal class SizeableImageConverter {

    private val photoAdapter: JsonAdapter<List<SizablePhoto>> = Moshi.Builder().build().run {
        adapter(Types.newParameterizedType(List::class.java, SizablePhoto::class.java))
    }

    @TypeConverter
    fun fromList(value: List<SizablePhoto>): String = photoAdapter.toJson(value)

    @TypeConverter
    fun toSizeableImages(data: String) = photoAdapter.fromJson(data)
}