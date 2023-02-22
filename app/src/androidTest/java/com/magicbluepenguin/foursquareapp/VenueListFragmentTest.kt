package com.magicbluepenguin.foursquareapp

import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasFocus
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import com.magicbluepenguin.foursquareapp.application.AppModule
import com.magicbluepenguin.foursquareapp.application.MainActivity
import com.magicbluepenguin.foursquareapp.util.submitSearch
import com.magicbluepenguin.repository.model.VenueListItem
import com.magicbluepenguin.repository.repositories.ErrorResponse
import com.magicbluepenguin.repository.repositories.FakeVenueSearchRepository
import com.magicbluepenguin.repository.repositories.SuccessResponse
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import com.magicbluepenguin.utils.extensions.doOnNetworkAvailable
import com.magicbluepenguin.utils.extensions.isNetworkAvailable
import com.magicbluepenguin.utils.test.android.checkTextViewIsDisplayed
import com.magicbluepenguin.utils.test.android.getFragment
import com.magicbluepenguin.utils.test.android.typeSearchViewText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
@UninstallModules(AppModule::class)
internal class VenueListFragmentTest {

    @BindValue
    val mockLocationSearchRepository: VenueSearchRepository = mockk<FakeVenueSearchRepository> {
        coEvery { findVenuesNearLocation(any()) } answers { SuccessResponse(emptyList()) }
    }

    @get:Rule
    val chain = RuleChain
        .outerRule(HiltAndroidRule(this))
        .around(activityScenarioRule<MainActivity>())

    private val fakeVenueListItem = VenueListItem("abc", "Cool Venue", "Address Line 1\nAddress Line 1\nAddress Line 1")

    @Test
    fun search_view_loses_focus_on_submit() {
        val expectedSearchQuery = "random search"

        val searchViewInteraction = onView(
            allOf(
                withId(R.id.search),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed(),
            )
        )

        searchViewInteraction.perform(
            typeSearchViewText(expectedSearchQuery, true),
        )

        searchViewInteraction.check(matches(not(hasFocus())))
    }

    @Test
    fun list_item_displays_title_and_formatted_adress() {
        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
            SuccessResponse(listOf(fakeVenueListItem))
        }

        submitSearch("Random")

        with(fakeVenueListItem) {
            checkTextViewIsDisplayed(R.id.venue_name, name)
            checkTextViewIsDisplayed(R.id.venue_location, address)
        }
    }

    @Test
    fun generic_error_is_not_displayed_when_cache_is_available() {
        testError(
            textViewRes = R.id.no_results_text,
            expectedText = R.string.venue_search_no_items_error,
            errorTextViewIsVisible = false,
            isNetworkAvailable = true,
            hasCachedData = true
        )
    }

    @Test
    fun generic_error_is_displayed_when_cache_is_not_available() {
        testError(
            textViewRes = R.id.no_results_text,
            expectedText = R.string.venue_search_no_items_error,
            errorTextViewIsVisible = true,
            isNetworkAvailable = true,
            hasCachedData = false
        )
    }

    @Test
    fun network_error_retry_text_is_displayed_when_cache_is_not_available() {
        testError(
            textViewRes = R.id.network_error_text,
            expectedText = R.string.venue_search_internet_connection_error,
            errorTextViewIsVisible = true,
            isNetworkAvailable = false,
            hasCachedData = false
        )
    }

    @Test
    fun network_error_update_text_is_displayed_when_cache_is_available() {
        testError(
            textViewRes = R.id.network_error_text,
            expectedText = R.string.venue_search_internet_connection_error_with_results,
            errorTextViewIsVisible = true,
            isNetworkAvailable = false,
            hasCachedData = true
        )
    }

    private fun testError(
        @IdRes textViewRes: Int,
        @StringRes expectedText: Int,
        errorTextViewIsVisible: Boolean,
        isNetworkAvailable: Boolean,
        hasCachedData: Boolean
    ) {
        // Static mocks are not supported after Android P
        assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)

        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
            ErrorResponse(if (hasCachedData) listOf(fakeVenueListItem) else null)
        }

        mockkStatic("com.magicbluepenguin.utils.extensions.FragmentExtensionsKt")
        every { getFragment().doOnNetworkAvailable(any()) } answers { }
        mockkStatic("com.magicbluepenguin.utils.extensions.ContextExtensionsKt")
        every { getFragment().requireContext().isNetworkAvailable() } answers { isNetworkAvailable }

        submitSearch("something")

        onView(
            allOf(
                withId(textViewRes),
                withText(expectedText)
            )
        ).check(
            matches(if (errorTextViewIsVisible) isDisplayed() else not(isDisplayed()))
        )
    }

}