package com.magicbluepenguin.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.magicbluepenguin.repository.model.SizablePhoto
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Database(entities = [VenueListItem::class, QueryModel::class, QueryWithVenue::class, VenueDetail::class], version = 1)
@TypeConverters(SizeableImageConverter::class)
abstract class VenueSearchDatabase : RoomDatabase() {
    internal abstract fun locationSearchDao(): VenueSearchDao
}

class SizeableImageConverter {

    private val photoAdapter: JsonAdapter<List<SizablePhoto>> = Moshi.Builder().build().run {
        adapter(Types.newParameterizedType(List::class.java, SizablePhoto::class.java))
    }

    @TypeConverter
    fun fromList(value: List<SizablePhoto>): String = photoAdapter.toJson(value)

    @TypeConverter
    fun toSizeableImages(data: String) = photoAdapter.fromJson(data)
}