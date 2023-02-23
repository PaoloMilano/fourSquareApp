package com.magicbluepenguin.features.venuesearch.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import com.magicbluepenguin.entities.venuesearch.VenueDetail
import com.magicbluepenguin.features.venuesearch.FakeVenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.VenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.response.NetworkError
import com.magicbluepenguin.repositories.venuesearch.response.RepositoryResponse
import com.magicbluepenguin.venuesearch.R
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Rule
import org.junit.Test

internal class VenueDetailScreenTest {

    private val mockLocationSearchRepository: VenueSearchRepository = mockk<FakeVenueSearchRepository>()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun venue_data_is_correctly_displayed() {
        val venueDetail = VenueDetail(
            id = "",
            name = "Venue name",
            description = "Venue desc",
            photos = emptyList(),
            formattedPhoneNumber = "062345678",
            address = "Venue address",
            rating = 4.7
        )
        coEvery { mockLocationSearchRepository.getVenueDetails(any()) } answers {
            RepositoryResponse(data = venueDetail)
        }

        with(composeTestRule) {

            setContent {
                VenueDetailScreen(
                    VenueDetailViewModel(
                        SavedStateHandle(mapOf(VENUE_ID_PARAM to "venue_id")),
                        mockLocationSearchRepository
                    )
                )
            }

            with(composeTestRule.activity) {
                with(venueDetail) {
                    onNodeWithText(getString(R.string.venue_name)).assertIsDisplayed()
                    onNodeWithText(name).assertIsDisplayed()
                    onNodeWithText(getString(R.string.venue_description)).assertIsDisplayed()
                    onNodeWithText(description).assertIsDisplayed()
                    onNodeWithText(getString(R.string.venue_address)).assertIsDisplayed()
                    onNodeWithText(address).assertIsDisplayed()
                    onNodeWithText(getString(R.string.venue_formattedPhoneNumber)).assertIsDisplayed()
                    onNodeWithText(formattedPhoneNumber).assertIsDisplayed()

                    onNodeWithText(getString(R.string.venue_details_available_with_error)).assertDoesNotExist()
                }
            }
        }
    }

    @Test
    fun network_error_is_displayed_alongside_content_when_present() {
        val venueDetail = VenueDetail(
            id = "",
            name = "Venue name",
            description = "Venue desc",
            photos = emptyList(),
            formattedPhoneNumber = "062345678",
            address = "Venue address",
            rating = 4.7
        )
        coEvery { mockLocationSearchRepository.getVenueDetails(any()) } answers {
            RepositoryResponse(data = venueDetail, error = NetworkError)
        }

        with(composeTestRule) {

            setContent {
                VenueDetailScreen(
                    VenueDetailViewModel(
                        SavedStateHandle(mapOf(VENUE_ID_PARAM to "venue_id")),
                        mockLocationSearchRepository
                    )
                )
            }

            with(composeTestRule.activity) {
                with(venueDetail) {
                    onNodeWithText(getString(R.string.venue_name)).assertIsDisplayed()
                    onNodeWithText(name).assertIsDisplayed()
                    onNodeWithText(getString(R.string.venue_description)).assertIsDisplayed()
                    onNodeWithText(description).assertIsDisplayed()
                    onNodeWithText(getString(R.string.venue_address)).assertIsDisplayed()
                    onNodeWithText(address).assertIsDisplayed()
                    onNodeWithText(getString(R.string.venue_formattedPhoneNumber)).assertIsDisplayed()
                    onNodeWithText(formattedPhoneNumber).assertIsDisplayed()

                    onNodeWithText(getString(R.string.venue_details_available_with_error)).assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun a_generic_error_is_displayed_when_no_data_is_available() {
        coEvery { mockLocationSearchRepository.getVenueDetails(any()) } answers {
            RepositoryResponse(data = null, error = NetworkError)
        }

        with(composeTestRule) {

            setContent {
                VenueDetailScreen(
                    VenueDetailViewModel(
                        SavedStateHandle(mapOf(VENUE_ID_PARAM to "venue_id")),
                        mockLocationSearchRepository
                    )
                )
            }

            with(composeTestRule.activity) {
                onNodeWithText(getString(R.string.venue_name)).assertDoesNotExist()
                onNodeWithText(getString(R.string.venue_description)).assertDoesNotExist()
                onNodeWithText(getString(R.string.venue_address)).assertDoesNotExist()
                onNodeWithText(getString(R.string.venue_formattedPhoneNumber)).assertDoesNotExist()

                onNodeWithText(getString(R.string.venue_details_unavailable_with_error)).assertIsDisplayed()
            }
        }
    }
}