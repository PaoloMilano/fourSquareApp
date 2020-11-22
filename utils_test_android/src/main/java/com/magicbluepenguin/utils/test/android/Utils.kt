package com.magicbluepenguin.utils.test.android

import androidx.appcompat.app.AppCompatActivity
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

fun getActivity(): AppCompatActivity {
    var activity: AppCompatActivity? = null
    getInstrumentation().runOnMainSync {
        activity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) as AppCompatActivity
    }
    return activity!!
}

fun getFragment() = getActivity().supportFragmentManager.fragments.first().run {
    // Do this check in case the fragment is displayed inside a NavHost
    if (childFragmentManager.fragments.isEmpty()) {
        this
    } else {
        childFragmentManager.fragments.first()
    }!!
}
