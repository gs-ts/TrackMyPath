package com.gts.flickrflow

import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.assertion.ViewAssertions

import com.gts.flickrflow.presentation.MainActivity

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun whenLaunchApp_ThenShowRightView() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(ViewMatchers.withId(R.id.buttonStart)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )

        onView(ViewMatchers.withId(R.id.imageRecyclerView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}