package com.silverorange.videoplayer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@LargeTest
class SimpleInstrumentedUITest {

    // espresso bug in api 33
    // https://github.com/android/android-test/issues/1412

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun playButtonTest(){
        onView(withId(R.id.fabPlay)).perform(click())
    }

    @Test
    fun playButtonIsEnabled(){
        onView(withId(R.id.fabPlay)).check(matches(isEnabled()))
    }

    @Test
    fun nextButtonIsEnabled(){
        onView(withId(R.id.fabNext)).check(matches(isEnabled()))
    }

    @Test
    fun previousButtonIsDisabled(){
        onView(withId(R.id.fabPrevious)).check(matches(isNotEnabled()))
    }

    @Test
    fun videoDescriptionIsNotEmpty() {
        onView(withId(R.id.etVideoDescr)).check(matches(not(notNullValue())))
    }

    @Test
    fun scrollViewIsNotEmpty() {
        onView(withId(R.id.scrollView)).check(matches(isEnabled()))
    }

    @Test
    fun scrollViewIsWorking() {
        onView(withId(R.id.scrollView)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()))
    }
}
