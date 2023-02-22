package com.magicbluepenguin.foursquareapp

import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import com.magicbluepenguin.foursquareapp.application.AppModule
import com.magicbluepenguin.foursquareapp.application.MainActivity
import com.magicbluepenguin.foursquareapp.util.submitSearch
import com.magicbluepenguin.repository.model.VenueDetail
import com.magicbluepenguin.repository.model.VenueListItem
import com.magicbluepenguin.repository.repositories.ErrorResponse
import com.magicbluepenguin.repository.repositories.FakeVenueSearchRepository
import com.magicbluepenguin.repository.repositories.SuccessResponse
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import com.magicbluepenguin.utils.extensions.doOnNetworkAvailable
import com.magicbluepenguin.utils.extensions.isNetworkAvailable
import com.magicbluepenguin.utils.test.android.checkTextViewIsDisplayed
import com.magicbluepenguin.utils.test.android.checkToolbaTitle
import com.magicbluepenguin.utils.test.android.getActivity
import com.magicbluepenguin.utils.test.android.getFragment
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
@UninstallModules(AppModule::class)
internal class VenueDetailFragmentTest {

    @BindValue
    val mockLocationSearchRepository: VenueSearchRepository = mockk<FakeVenueSearchRepository> {
        coEvery { findVenuesNearLocation(any()) } answers { SuccessResponse(emptyList()) }
    }
    private val fakeVenueItem = VenueListItem(
        id = "loc_id",
        name = "",
        address = ""
    )
    private val fakeVenueDetail = VenueDetail(
        id = "",
        name = "Venue name",
        description = "Venue desc",
        photos = emptyList(),
        formattedPhoneNumber = "062345678",
        address = "Venue address",
        rating = 4.7
    )

    @get:Rule
    val chain = RuleChain
        .outerRule(HiltAndroidRule(this))
        .around(activityScenarioRule<MainActivity>())

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun clicking_on_list_item_opens_detail_screen() {
        val locationQuery = "Randomcity"
        prepareFakeListItemResponse()
        prepareFakeDetailResponse()

        submitSearch(locationQuery)
        clickOnFirstListItem()

        val displayingChildFragments = getActivity().supportFragmentManager.fragments[0].childFragmentManager.fragments
        assertEquals(1, displayingChildFragments.size)
        assertTrue(displayingChildFragments.first() is VenueDetailFragment)

        with(fakeVenueDetail) {
            checkToolbaTitle(R.id.toolbar, name)
            checkTextViewIsDisplayed(R.id.venue_address, address)
            checkTextViewIsDisplayed(R.id.venue_phone_number, formattedPhoneNumber)
            checkTextViewIsDisplayed(R.id.venue_rating, getActivity().resources.getString(R.string.venue_rating_sf, rating))

            unmockkStatic(Dispatchers::class)
            coVerifySequence {
                mockLocationSearchRepository.findVenuesNearLocation(locationQuery)
                mockLocationSearchRepository.getVenueDetails(fakeVenueItem.id)
            }
        }
    }

    @Test
    fun generic_data_errors_are_displayed_to_the_user_when_no_cache_is_available() {
        testError(
            textViewRes = R.id.no_results_text,
            expectedText = R.string.venue_detail_generic_error,
            errorTextViewIsVisible = true,
            isNetworkAvailable = true,
            hasCachedData = false
        )
    }

    @Test
    fun generic_data_errors_are_not_displayed_to_the_user_when_cache_is_available() {
        testError(
            textViewRes = R.id.no_results_text,
            expectedText = R.string.venue_detail_generic_error,
            errorTextViewIsVisible = false,
            isNetworkAvailable = true,
            hasCachedData = true
        )
    }

    @Test
    fun network_errors_are_displayed_to_the_user_with_no_cached_data() {
        testError(
            textViewRes = R.id.network_error_text,
            expectedText = R.string.venue_detail_internet_connection_error,
            errorTextViewIsVisible = true,
            isNetworkAvailable = false,
            hasCachedData = false
        )
    }

    @Test
    fun network_errors_are_displayed_to_the_user_with_cached_data() {
        testError(
            textViewRes = R.id.network_error_text,
            expectedText = R.string.venue_detail_internet_connection_error_with_result,
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

        prepareFakeListItemResponse()

        coEvery { mockLocationSearchRepository.getVenueDetails(any()) } answers {
            // We do this here because the VenueDetailFragment will only be pushed onto the stack after the call to `clickOnFirstListItem()`.
            // At this point however it will immediately perform a call to fetch data so we have to set these mocks between these two events.
            mockkStatic("com.magicbluepenguin.utils.extensions.FragmentExtensionsKt")
            every { getFragment().doOnNetworkAvailable(any()) } answers { }
            mockkStatic("com.magicbluepenguin.utils.extensions.ContextExtensionsKt")
            every { getFragment().requireContext().isNetworkAvailable() } answers { isNetworkAvailable }
            ErrorResponse(if (hasCachedData) fakeVenueDetail else null)
        }

        submitSearch("something")
        clickOnFirstListItem()

        onView(
            allOf(
                withId(textViewRes),
                withText(expectedText)
            )
        ).check(
            matches(if (errorTextViewIsVisible) isDisplayed() else not(isDisplayed()))
        )
    }


    private fun clickOnFirstListItem() {
        onView(withId(R.id.search_results_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    private fun prepareFakeListItemResponse() {
        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
            SuccessResponse(listOf(fakeVenueItem))
        }
    }

    private fun prepareFakeDetailResponse() {
        coEvery { mockLocationSearchRepository.getVenueDetails(any()) } answers {
            SuccessResponse(fakeVenueDetail)
        }
    }
}