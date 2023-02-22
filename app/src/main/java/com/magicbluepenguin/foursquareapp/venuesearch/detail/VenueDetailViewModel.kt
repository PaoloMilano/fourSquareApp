package com.magicbluepenguin.foursquareapp.venuesearch.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.repository.repositories.venuesearch.VenueSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class VenueDetailState
object Loading : VenueDetailState()
data class DataRetrieved(val venue: VenueDetail?, val withError: Boolean = false) : VenueDetailState()
data class EmptyResult(val withError: Boolean = false) : VenueDetailState()

@HiltViewModel
internal class VenueDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val venueSearchRepository: VenueSearchRepository
) : ViewModel() {

    val venueState = MutableStateFlow<VenueDetailState?>(null)

    init {
        savedStateHandle.get<String>(VENUE_ID_PARAM)?.let { venueId ->
            venueState.value = Loading
            viewModelScope.launch {
                with(venueSearchRepository.getVenueDetails(venueId)) {
                    venueState.value = if (data != null) {
                        DataRetrieved(data, error != null)
                    } else {
                        EmptyResult(error != null)
                    }
                }
            }
        } ?: throw IllegalArgumentException("A venueId parameter must be supplied")
    }
}

const val VENUE_ID_PARAM = "venue_id"