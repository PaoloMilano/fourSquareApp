package com.magicbluepenguin.foursquareapp.venuesearch.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.repository.repositories.venuesearch.VenueSearchRepository
import com.magicbluepenguin.repository.response.ErrorCause
import com.magicbluepenguin.repository.response.Network
import com.magicbluepenguin.repository.response.RepositoryResponse
import com.magicbluepenguin.utils.extensions.NetworkChangeReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class VenueListState
object Loading : VenueListState()
object EmptyResult : VenueListState()

data class DataRetrieved(val venues: List<VenueListItem>, val errorCause: ErrorCause? = null) :VenueListState()

@HiltViewModel
internal class VenueSearchViewModel @Inject constructor(
    private val venueSearchRepository: VenueSearchRepository,
    private val networkChangeReceiver: NetworkChangeReceiver
) : ViewModel() {

    val venueState = MutableStateFlow<VenueListState?>(null)

    fun submitSearch(searchQuery: String) {
        venueState.value = Loading
        viewModelScope.launch {
            searchQuery.let {
                venueState.value = venueSearchRepository.findVenuesNearLocation(it).run {
                    when {
                        error != null -> {
                            if (error == Network) {
                                networkChangeReceiver.doOnNetworkAvailable {
                                    submitSearch(searchQuery)
                                }
                            }
                            DataRetrieved(data, error!!)
                        }
                        data.isEmpty() -> EmptyResult
                        else -> DataRetrieved(data)
                    }
                }
            }
        }
    }
}