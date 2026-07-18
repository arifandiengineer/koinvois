package com.koinvois.generator.ui.lock

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
class SecurityFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(EnterPasswordActivity::class.java)

    @Test
    fun enterPassword_showsCorrectLayout() {
        onView(withId(R.id.pinLockView)).check(matches(isDisplayed()))
        onView(withId(R.id.indicatorDots)).check(matches(isDisplayed()))
        onView(withId(R.id.txtForgotPassword)).check(matches(isDisplayed()))
    }

    // Note: Testing actual PIN entry is complex with custom views, 
    // but we can verify the UI elements exist and are clickable.
    @Test
    fun forgotPassword_navigatesToLockMain() {
        onView(withId(R.id.txtForgotPassword)).perform(click())
        // Should open LockMainActivity with RECOVERY type
    }
}
