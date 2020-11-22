package com.magicbluepenguin.repository.model

data class SizablePhoto(val photoPrefix: String, val photoSuffix: String) {

    // Available sizes as defined here: https://developer.foursquare.com/docs/api-reference/venues/photos/
    private val availabelSizes = listOf(36, 100, 300, 500)

    fun photoForSize(width: Int, height: Int): String {
        fun Int.nextSizeUp() = availabelSizes.firstOrNull { it >= this } ?: availabelSizes.last()
        return "$photoPrefix${width.nextSizeUp()}x${height.nextSizeUp()}$photoSuffix"
    }
}
