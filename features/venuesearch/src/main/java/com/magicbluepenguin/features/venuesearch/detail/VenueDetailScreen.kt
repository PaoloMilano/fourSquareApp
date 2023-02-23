package com.magicbluepenguin.features.venuesearch.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.magicbluepenguin.entities.venuesearch.SizablePhoto
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.shared.composables.EmptyResultMessage
import com.magicbluepenguin.shared.composables.FullScreenProgress
import com.magicbluepenguin.venuesearch.R
import kotlin.math.min

@Composable
internal fun VenueDetailScreen(viewModel: VenueDetailViewModel = hiltViewModel()) {

    val venueState by viewModel.venueState.collectAsState()

    when (val state = venueState) {
        null -> EmptyResultMessage(messageText = stringResource(id = R.string.no_results_yet_message))
        Loading -> FullScreenProgress()
        is DataRetrieved -> VenueDetail(
            venueDetail = state.venue,
            errorMessage = if (state.withError) {
                stringResource(R.string.venue_details_available_with_error)
            } else {
                null
            }
        )
        is EmptyResult -> {
            EmptyResultMessage(
                messageText = stringResource(
                    id =
                    if (state.withError) {
                        R.string.venue_details_unavailable_with_error
                    } else {
                        R.string.venue_details_unavailable
                    }
                )
            )
        }
    }
}

@Composable
internal fun VenueDetail(venueDetail: VenueDetail?, errorMessage: String?) {
    Column(modifier = Modifier.fillMaxSize()) {
        venueDetail?.let {
            VenueDetailContainer(modifier = Modifier.weight(1f), venueDetail = it)
        } ?: Spacer(modifier = Modifier.weight(1f))
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
internal fun VenueDetailContainer(modifier: Modifier, venueDetail: VenueDetail) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        if (venueDetail.photos.isNotEmpty()) {
            HorizontalImageList(sizablePhotos = venueDetail.photos, venueName = venueDetail.name)
        } else {
            PlaceHolderImage()
        }

        Spacer(modifier = Modifier.height(12.dp))

        VenueDetailRow(headerId = R.string.venue_name, value = venueDetail.name)
        VenueDetailRow(headerId = R.string.venue_description, value = venueDetail.description)
        VenueDetailRow(headerId = R.string.venue_address, value = venueDetail.address)
        VenueDetailRow(headerId = R.string.venue_formattedPhoneNumber, value = venueDetail.formattedPhoneNumber)
    }
}

@Composable
internal fun VenueDetailRow(@StringRes headerId: Int, value: String) {
    if (value.isNotEmpty()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .weight(2f)
                    .padding(12.dp), fontStyle = FontStyle.Italic, text = stringResource(id = headerId)
            )
            Text(
                modifier = Modifier
                    .weight(3f)
                    .padding(12.dp), text = value
            )
        }
    }
}

@Composable
fun HorizontalImageList(venueName: String, sizablePhotos: List<SizablePhoto>) {
    val photoWidth = LocalConfiguration.current.run { min(screenHeightDp, screenWidthDp) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(photoWidth.dp)
    ) {
        items(sizablePhotos) { sizablePhoto ->
            AsyncImage(
                modifier = Modifier
                    .width(photoWidth.dp)
                    .height(photoWidth.dp),
                contentScale = ContentScale.Fit,
                model = sizablePhoto.photoForSize(photoWidth, photoWidth),
                contentDescription = stringResource(id = R.string.venue_image_content_description, venueName),
                error = painterResource(R.drawable.placeholder)
            )
        }
    }
}

@Composable
fun PlaceHolderImage() {
    val photoWidth = LocalConfiguration.current.run { min(screenHeightDp, screenWidthDp) }
    Image(
        modifier = Modifier
            .width(photoWidth.dp)
            .height(photoWidth.dp),
        contentScale = ContentScale.Fit,
        painter = painterResource(R.drawable.placeholder),
        contentDescription = stringResource(id = R.string.placeholder_image_content_description),
    )
}