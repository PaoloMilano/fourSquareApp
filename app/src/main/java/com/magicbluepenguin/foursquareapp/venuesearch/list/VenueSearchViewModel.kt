package com.magicbluepenguin.foursquareapp.venuesearch.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.repository.model.Venue
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class VenueSearchViewModel @ViewModelInject constructor(private val venueSearchRepository: VenueSearchRepository) : ViewModel() {

    var searchQuery: String? = null

    val venuesLiveData = MutableLiveData<List<Venue>>()

    fun submitSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQuery?.let {
                venuesLiveData.postValue(venueSearchRepository.findVenuesNearLocation(it))
            }
        }
    }
}