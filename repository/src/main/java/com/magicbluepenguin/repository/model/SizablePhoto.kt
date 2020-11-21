package com.magicbluepenguin.repository.model

data class SizablePhoto(val photoUrl: String) {
    fun photoForSize(width: Int, height: Int) = String.format(photoUrl, width, height)
}
