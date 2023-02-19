package com.magicbluepenguin.foursquareapp.venuesearch.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.repository.model.VenueListItem
import com.magicbluepenguin.repository.repositories.RepositoryResponse
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class VenueSearchViewModel @Inject constructor(private val venueSearchRepository: VenueSearchRepository) : ViewModel() {

    var searchQuery: String? = null

    val venuesLiveData = MutableLiveData<RepositoryResponse<List<VenueListItem>>>()
    val searchInProgressLiveData = MutableLiveData<Boolean>()

    fun submitSearch() {
        searchInProgressLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            searchQuery?.let {
                searchInProgressLiveData.postValue(false)
                venuesLiveData.postValue(venueSearchRepository.findVenuesNearLocation(it))
            }
        }
    }
}