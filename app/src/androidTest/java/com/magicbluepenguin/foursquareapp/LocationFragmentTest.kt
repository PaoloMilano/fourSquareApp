package com.magicbluepenguin.foursquareapp

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasFocus
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.magicbluepenguin.foursquareapp.application.ApiConfigModule
import com.magicbluepenguin.foursquareapp.application.MainActivity
import com.magicbluepenguin.foursquareapp.venuesearch.detail.VenueDetailFragment
import com.magicbluepenguin.repository.model.VenueListItem
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import com.magicbluepenguin.utils.test.android.typeSearchViewText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
@UninstallModules(ApiConfigModule::class)
internal class LocationFragmentTest {

    @BindValue
    @JvmField
    val mockLocationSearchRepository: VenueSearchRepository = mockk {
        coEvery { findVenuesNearLocation(any()) } answers { emptyList() }
    }

    @get:Rule
    val chain = RuleChain
        .outerRule(HiltAndroidRule(this))
        .around(activityScenarioRule<MainActivity>())

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
        val venueName = "Cool Venue"
        val venueAddress = "Address Line 1\nAddress Line 1\nAddress Line 1"

        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
            listOf(VenueListItem("abc", venueName, venueAddress))
        }

        onView(
            allOf(
                withId(R.id.search),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed(),
            )
        ).perform(
            typeSearchViewText("Randomcity", true),
        )

        onView(
            allOf(
                withId(R.id.venue_name),
                withText(venueName)
            )
        ).check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.venue_location),
                withText(venueAddress),
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun clicking_on_list_item_opens_detail_screen() {
        val locationQuery = "Randomcity"
        val locationId = "loc_id"

        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
            listOf(VenueListItem(locationId, "", ""))
        }
        coEvery { mockLocationSearchRepository.getVenueDetails(any()) } answers { mockk() }

        onView(
            allOf(
                withId(R.id.search),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed(),
            )
        ).perform(
            typeSearchViewText(locationQuery, true),
        )

        onView(withId(R.id.searchResultsRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        getInstrumentation().runOnMainSync {
            val activity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) as AppCompatActivity
            val displayingChildFragments = activity.supportFragmentManager.fragments[0].childFragmentManager.fragments

            assertEquals(1, displayingChildFragments.size)
            assertTrue(displayingChildFragments.first() is VenueDetailFragment)
        }
        coVerifySequence {
            mockLocationSearchRepository.findVenuesNearLocation(locationQuery)
            mockLocationSearchRepository.getVenueDetails(locationId)
        }
    }
}
