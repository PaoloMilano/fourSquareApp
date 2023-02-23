package com.magicbluepenguin.entities.venuesearch

import org.junit.Assert.assertEquals
import org.junit.Test

internal class SizablePhotoTest {

    private val prefix = "prefix"
    private val suffix = "suffix"
    private val testPhoto = SizablePhoto(prefix, suffix)

    @Test
    fun `photo is set to have the same size as the requested diemnsions when available`() {
        assertEquals(sizedString(36, 36), testPhoto.photoForSize(36, 36))
        assertEquals(sizedString(100, 100), testPhoto.photoForSize(100, 100))
        assertEquals(sizedString(300, 300), testPhoto.photoForSize(300, 300))
        assertEquals(sizedString(500, 500), testPhoto.photoForSize(500, 500))
    }

    @Test
    fun `photo is set to have the next size larger than the requested diemnsions when not available`() {
        assertEquals(sizedString(36, 36), testPhoto.photoForSize(34, 16))
        assertEquals(sizedString(100, 100), testPhoto.photoForSize(90, 50))
        assertEquals(sizedString(300, 300), testPhoto.photoForSize(200, 290))
        assertEquals(sizedString(500, 500), testPhoto.photoForSize(450, 489))
    }

    @Test
    fun `photo is set to have the largest size possible for very large image sizes`() {
        assertEquals(sizedString(500, 500), testPhoto.photoForSize(600, 700))
    }

    private fun sizedString(width: Int, height: Int) = "$prefix${width}x$height$suffix"

}
