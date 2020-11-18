package com.magicbluepenguin.foursquareapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasFocus
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.ext.junit.rules.activityScenarioRule
import com.magicbluepenguin.utils.test.android.typeSearchViewText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
internal class LocationFragmentTest {

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
}
