package com.magicbluepenguin.foursquareapp.venuesearch.detail

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.magicbluepenguin.repository.model.SizablePhoto
import com.magicbluepenguin.utils.extensions.heightDP
import com.magicbluepenguin.utils.extensions.widthDP


internal class VenuePhotosAdapter(private val sizablePhotos: List<SizablePhoto>) : PagerAdapter() {
    override fun getCount(): Int = sizablePhotos.size

    override fun isViewFromObject(view: View, item: Any): Boolean = (item as? ImageView) == view

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return ImageView(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            doOnLayout {
                Glide.with(this)
                    .load(sizablePhotos[position].photoForSize(widthDP(), heightDP()))
                    .into(this)
            }
            container.addView(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as ImageView)
    }
}