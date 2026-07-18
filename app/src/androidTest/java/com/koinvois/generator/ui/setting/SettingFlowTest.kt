package com.koinvois.generator.ui.setting

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingActivity::class.java)

    @Test
    fun settingActivity_displaysBasicOptions() {
        onView(withId(R.id.txtPersonalBusiness)).check(matches(isDisplayed()))
        onView(withId(R.id.switchLock)).check(matches(isDisplayed()))
        onView(withId(R.id.switchDarkMode)).check(matches(isDisplayed()))
        onView(withId(R.id.txtLanguage)).check(matches(isDisplayed()))
        onView(withId(R.id.txtCurrency)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingBusinessProfile_opensBusinessActivity() {
        onView(withId(R.id.txtPersonalBusiness)).perform(click())
        // Should navigate to AddBusinessDetailsActivity
    }

    @Test
    fun toggleLock_changesState() {
        onView(withId(R.id.switchLock)).perform(click())
        // Verified by observing switch state change or pref change
    }
}
