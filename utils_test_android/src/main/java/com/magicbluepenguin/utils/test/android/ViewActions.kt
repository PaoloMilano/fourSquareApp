package com.magicbluepenguin.utils.test.android

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

fun typeSearchViewText(text: String, submit: Boolean): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Change view text"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as SearchView).setQuery(text, submit)
        }
    }
}

fun checkTextViewIsDisplayed(@LayoutRes withTextViewId: Int, withText: String) {
    onView(
        allOf(
            withId(withTextViewId),
            withText(withText),
        )
    ).check(matches(isDisplayed()))
}

fun checkToolbaTitle(@LayoutRes toolbarId: Int, withText: String) {
    onView(withId(toolbarId)).check(matches(ViewMatchers.hasDescendant(withText(withText))))
}