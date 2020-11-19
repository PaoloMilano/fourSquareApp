package com.magicbluepenguin.foursquareapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.repository.repositories.LocationSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class LocationSearchViewModel @ViewModelInject constructor(private val locationSearchRepository: LocationSearchRepository) : ViewModel() {

    var searchQuery: String? = null

    fun submitSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQuery?.let {
                locationSearchRepository.findVenuesNearLocation(it)
            }
        }
    }
}