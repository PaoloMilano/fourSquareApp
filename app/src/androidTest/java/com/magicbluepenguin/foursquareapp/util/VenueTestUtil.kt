package com.magicbluepenguin.foursquareapp.util

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.magicbluepenguin.foursquareapp.R
import com.magicbluepenguin.utils.test.android.typeSearchViewText
import org.hamcrest.CoreMatchers.allOf

fun submitSearch(locationQuery: String) {
    onView(
        allOf(
            withId(R.id.search),
            withParent(withParent(withId(R.id.toolbar))),
            isDisplayed(),
        )
    ).perform(
        typeSearchViewText(locationQuery, true),
    )
}