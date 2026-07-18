package com.koinvois.generator.ui.setting

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.setting.add_business.AddBusinessDetailsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BusinessFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddBusinessDetailsActivity::class.java)

    @Test
    fun businessDetails_fillsDataAndSaves() {
        onView(withId(R.id.editBusinessName)).perform(typeText("Audit Company"), closeSoftKeyboard())
        onView(withId(R.id.editBusinessOwnerName)).perform(typeText("Audit Owner"), closeSoftKeyboard())
        onView(withId(R.id.editEmail)).perform(typeText("audit@company.com"), closeSoftKeyboard())
        onView(withId(R.id.editWebsite)).perform(typeText("www.audit.com"), closeSoftKeyboard())
        
        // This triggers saveOnBack and finish
        onView(withId(R.id.customToolbar)).perform(click()) // Click back button in toolbar
    }
}
