package com.magicbluepenguin.foursquareapp.venuesearch.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magicbluepenguin.foursquareapp.databinding.ViewHolderVenueItemBinding
import com.magicbluepenguin.repository.model.Venue

internal class VenueListAdapter : RecyclerView.Adapter<VenueListViewHolder>() {

    private var data = mutableListOf<Venue>()

    fun updateData(venues: List<Venue>) {
        data.clear()
        data.addAll(venues)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueListViewHolder =
        ViewHolderVenueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { VenueListViewHolder(this) }

    override fun onBindViewHolder(holder: VenueListViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

}

internal class VenueListViewHolder(private val viewBinding: ViewHolderVenueItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(venue: Venue) {
        viewBinding.venueName.text = venue.name
        viewBinding.venueLocation.text = venue.location
    }
}