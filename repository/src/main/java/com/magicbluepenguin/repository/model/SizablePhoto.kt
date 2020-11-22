package com.magicbluepenguin.repository.model

data class SizablePhoto(val photoPrefix: String, val photoSuffix: String) {

    fun photoForSize(width: Int, height: Int): String {
        fun Int.nextSizeUp() = AVAILABLE_SIZES.firstOrNull { it >= this } ?: AVAILABLE_SIZES.last()
        return "$photoPrefix${width.nextSizeUp()}x${height.nextSizeUp()}$photoSuffix"
    }
}

// Available sizes as defined here: https://developer.foursquare.com/docs/api-reference/venues/photos/
private val AVAILABLE_SIZES = listOf(36, 100, 300, 500)
