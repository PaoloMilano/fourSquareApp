package com.magicbluepenguin.foursquareapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasFocus
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import com.magicbluepenguin.foursquareapp.application.ApiConfigModule
import com.magicbluepenguin.repository.model.Venue
import com.magicbluepenguin.repository.model.VenueAddress
import com.magicbluepenguin.repository.repositories.VenueSearchRepository
import com.magicbluepenguin.utils.test.android.typeSearchViewText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
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
        val venueAddress = listOf("Address Line 1", "Address Line 1", "Address Line 1")

        coEvery { mockLocationSearchRepository.findVenuesNearLocation(any()) } answers {
            listOf(Venue("abc", venueName, VenueAddress(venueAddress)))
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
                withText(venueAddress.joinToString(separator = "\n")),
            )
        ).check(matches(isDisplayed()))
    }
}
