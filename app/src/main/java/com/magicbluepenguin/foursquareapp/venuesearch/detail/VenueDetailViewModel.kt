package com.magicbluepenguin.foursquareapp.venuesearch.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.repositories.RepositoryResponse
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class VenueDetailViewModel @ViewModelInject constructor(private val venueSearchRepository: VenueSearchRepository) : ViewModel() {

    val venuesLiveData = MutableLiveData<RepositoryResponse<VenueDetail?>>()
    val searchInProgressLiveData = MutableLiveData<Boolean>()

    fun fetchDetailsForVenue(venueId: String) {
        searchInProgressLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            searchInProgressLiveData.postValue(false)
            venuesLiveData.postValue(venueSearchRepository.getVenueDetails(venueId))
        }
    }
}