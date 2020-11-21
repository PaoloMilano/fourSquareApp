package com.magicbluepenguin.foursquareapp.venuesearch.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magicbluepenguin.foursquareapp.databinding.ViewHolderVenueItemBinding
import com.magicbluepenguin.repository.model.VenueListItem

internal class VenueListAdapter(private val onVenueClickedListener: (venuedId: String) -> Unit) : RecyclerView.Adapter<VenueListViewHolder>() {

    private var data = mutableListOf<VenueListItem>()

    fun updateData(venueListItems: List<VenueListItem>) {
        data.clear()
        data.addAll(venueListItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueListViewHolder =
        ViewHolderVenueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { VenueListViewHolder(this) }

    override fun onBindViewHolder(holder: VenueListViewHolder, position: Int) = holder.bind(data[position], onVenueClickedListener)

    override fun getItemCount(): Int = data.size

}

internal class VenueListViewHolder(private val viewBinding: ViewHolderVenueItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(venueListItem: VenueListItem, onVenueClickedListener: (venuedId: String) -> Unit) {
        viewBinding.venueName.text = venueListItem.name
        viewBinding.venueLocation.text = venueListItem.address
        viewBinding.root.setOnClickListener { onVenueClickedListener.invoke(venueListItem.id) }
    }
}