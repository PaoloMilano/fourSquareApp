package com.magicbluepenguin.features.venuesearch.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.repositories.venuesearch.response.NetworkError
import com.magicbluepenguin.repositories.venuesearch.response.ServerError
import com.magicbluepenguin.shared.composables.EmptyResultMessage
import com.magicbluepenguin.shared.composables.FullScreenProgress
import com.magicbluepenguin.venuesearch.R

@Composable
internal fun VenueSearchScreen(
    viewModel: VenueSearchViewModel = hiltViewModel(),
    onVenueSelected: (venueId: String) -> Unit
) {

    val venueListState by viewModel.venueState.collectAsState()

    Column {

        SearchView { searchTerm -> viewModel.submitSearch(searchTerm) }

        when (val state = venueListState) {
            null -> EmptyResultMessage(messageText = stringResource(id = R.string.no_results_yet_message))
            Loading -> FullScreenProgress()
            EmptyResult -> EmptyResultMessage(messageText = stringResource(id = R.string.no_results_found_message))
            is DataRetrieved -> {
                val errorMessage = when (state.errorCause) {
                    NetworkError -> stringResource(R.string.venue_search_internet_connection_error)
                    ServerError -> stringResource(R.string.venue_search_server_error)
                    null -> null
                }
                VenueList(state.venues, errorMessage) { venueItem -> onVenueSelected.invoke(venueItem.id) }
            }
        }
    }
}

@Composable
internal fun SearchView(onSearch: (searchTerm: String) -> Unit) {

    val searchState = rememberSaveable { mutableStateOf("") }
    val currentFocus = LocalFocusManager.current

    TextField(
        value = searchState.value,
        onValueChange = { value -> searchState.value = value },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(searchState.value)
            currentFocus.clearFocus()
        }),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier.padding(12.dp)
            )
        },
        trailingIcon = {
            if (searchState.value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        searchState.value = ""
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(0.55F)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
internal fun VenueList(venues: List<VenueListItem>, errorMessage: String? = null, onVenueSelected: (VenueListItem) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
        ) {
            items(venues) { venue ->
                VenueListItem(
                    venueItem = venue,
                    onItemClick = { onVenueSelected.invoke(venue) }
                )
            }
        }
        if (errorMessage != null) {
            Text(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.error)
                    .fillMaxWidth()
                    .padding(12.dp), text = errorMessage
            )
        }
    }
}

@Composable
internal fun VenueListItem(venueItem: VenueListItem, onItemClick: (VenueListItem) -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = { onItemClick(venueItem) })
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(PaddingValues(8.dp, 16.dp))
    ) {
        Text(text = venueItem.name)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = venueItem.address, fontStyle = FontStyle.Italic)
    }
}
