package com.magicbluepenguin.features.venuesearch.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import com.magicbluepenguin.entities.venuesearch.VenueListItem
import com.magicbluepenguin.features.venuesearch.FakeVenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.VenueSearchRepository
import com.magicbluepenguin.repositories.venuesearch.response.NetworkError
import com.magicbluepenguin.repositories.venuesearch.response.RepositoryResponse
import com.magicbluepenguin.utils.extensions.NetworkChangeReceiver
import com.magicbluepenguin.venuesearch.R
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

internal class VenueSearchScreenTest {

    val mockLocationSearchRepository: VenueSearchRepository = mockk<FakeVenueSearchRepository> {
        coEvery { findVenuesNearLocation(any()) } answers { RepositoryResponse(emptyList()) }
    }
    private val fakeVenueListItem = VenueListItem("abc", "Cool Venue", "Address Line 1\nAddress Line 1\nAddress Line 1")

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun empty_result_message_changes_from_before_to_after_searching() {
        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers { RepositoryResponse(emptyList()) }

        with(composeTestRule) {

            setContent {
                VenueSearchScreen(
                    viewModel = VenueSearchViewModel(mockLocationSearchRepository, mockk()),
                    onVenueSelected = {})
            }

            with(composeTestRule.activity) {
                onNodeWithText(getString(R.string.no_results_yet_message)).assertIsDisplayed()

                performSearch("")

                onNodeWithText(getString(R.string.no_results_yet_message)).assertDoesNotExist()
                onNodeWithText(getString(R.string.no_results_found_message)).assertIsDisplayed()
            }
        }
    }

    @Test
    fun successful_data_result_displays_data() {
        val expectedSearchQuery = "random search"
        coEvery {
            mockLocationSearchRepository.findVenuesNearLocation(expectedSearchQuery)
        } answers {
            RepositoryResponse(listOf(fakeVenueListItem))
        }

        with(composeTestRule) {

            setContent {
                VenueSearchScreen(
                    viewModel = VenueSearchViewModel(mockLocationSearchRepository, mockk()),
                    onVenueSelected = {})
            }

            performSearch(expectedSearchQuery)

            onNodeWithText(fakeVenueListItem.name).assertIsDisplayed()
            onNodeWithText(fakeVenueListItem.address).assertIsDisplayed()
            onNodeWithText(composeTestRule.activity.getString(R.string.venue_search_internet_connection_error)).assertDoesNotExist()
        }
    }

    @Test
    fun data_result_with_error_displays_error_alongside_data() {
        val expectedSearchQuery = "random search"
        coEvery {
            mockLocationSearchRepository.findVenuesNearLocation(expectedSearchQuery)
        } answers {
            RepositoryResponse(data = listOf(fakeVenueListItem), error = NetworkError)
        }

        with(composeTestRule) {

            setContent {
                VenueSearchScreen(viewModel = VenueSearchViewModel(
                    mockLocationSearchRepository,
                    mockk<NetworkChangeReceiver>(relaxed = true)
                ), onVenueSelected = {})
            }

            performSearch(expectedSearchQuery)

            onNodeWithText(fakeVenueListItem.name).assertIsDisplayed()
            onNodeWithText(fakeVenueListItem.address).assertIsDisplayed()
            onNodeWithText(composeTestRule.activity.getString(R.string.venue_search_internet_connection_error)).assertIsDisplayed()
        }
    }

    private fun performSearch(query: String) = with(composeTestRule) {
        onNode(hasImeAction(ImeAction.Search)).apply {
            performTextInput(query)
            performImeAction()
            assertIsNotFocused()
        }
    }
}

//        val searchViewInteraction = onView(
//            allOf(
//                withId(R.id.search),
//                withParent(withParent(withId(R.id.toolbar))),
//                isDisplayed(),
//            )
//        )
//
//        searchViewInteraction.perform(
//            typeSearchViewText(expectedSearchQuery, true),
//        )
//
//        searchViewInteraction.check(matches(not(hasFocus())))
//    }

//    @Test
//    fun list_item_displays_title_and_formatted_adress() {
//        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
//            RepositoryResponse(listOf(fakeVenueListItem))
//        }
//
//        submitSearch("Random")
//
//        with(fakeVenueListItem) {
//            checkTextViewIsDisplayed(R.id.venue_name, name)
//            checkTextViewIsDisplayed(R.id.venue_location, address)
//        }
//    }
//
//    @Test
//    fun generic_error_is_not_displayed_when_cache_is_available() {
//        testError(
//            textViewRes = R.id.no_results_text,
//            expectedText = R.string.venue_search_no_items_error,
//            errorTextViewIsVisible = false,
//            isNetworkAvailable = true,
//            hasCachedData = true
//        )
//    }
//
//    @Test
//    fun generic_error_is_displayed_when_cache_is_not_available() {
//        testError(
//            textViewRes = R.id.no_results_text,
//            expectedText = R.string.venue_search_no_items_error,
//            errorTextViewIsVisible = true,
//            isNetworkAvailable = true,
//            hasCachedData = false
//        )
//    }
//
//    @Test
//    fun network_error_retry_text_is_displayed_when_cache_is_not_available() {
//        testError(
//            textViewRes = R.id.network_error_text,
//            expectedText = R.string.venue_search_internet_connection_error,
//            errorTextViewIsVisible = true,
//            isNetworkAvailable = false,
//            hasCachedData = false
//        )
//    }
//
//    @Test
//    fun network_error_update_text_is_displayed_when_cache_is_available() {
//        testError(
//            textViewRes = R.id.network_error_text,
//            expectedText = R.string.venue_search_internet_connection_error_with_results,
//            errorTextViewIsVisible = true,
//            isNetworkAvailable = false,
//            hasCachedData = true
//        )
//    }
//
//    private fun testError(
//        @IdRes textViewRes: Int,
//        @StringRes expectedText: Int,
//        errorTextViewIsVisible: Boolean,
//        isNetworkAvailable: Boolean,
//        hasCachedData: Boolean
//    ) {
//        // Static mocks are not supported after Android P
//        assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
//
//        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
//            ErrorResponse(if (hasCachedData) listOf(fakeVenueListItem) else null)
//        }
//
//        mockkStatic("com.magicbluepenguin.utils.extensions.FragmentExtensionsKt")
//        every { getFragment().doOnNetworkAvailable(any()) } answers { }
//        mockkStatic("com.magicbluepenguin.utils.extensions.ContextExtensionsKt")
//        every { getFragment().requireContext().isNetworkAvailable() } answers { isNetworkAvailable }
//
//        submitSearch("something")
//
//        onView(
//            allOf(
//                withId(textViewRes),
//                withText(expectedText)
//            )
//        ).check(
//            matches(if (errorTextViewIsVisible) isDisplayed() else not(isDisplayed()))
//        )
//    }
//
//   }