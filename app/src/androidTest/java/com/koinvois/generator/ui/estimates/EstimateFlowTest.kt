package com.koinvois.generator.ui.estimates

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.estimates.add_estimate.AddEstimateMainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstimateFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddEstimateMainActivity::class.java)

    @Test
    fun estimateEditor_displaysBasicFields() {
        onView(withId(R.id.txtEstimateNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.txtEstimateDate)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveEstimate)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingTax_opensTaxActivity() {
        onView(withId(R.id.txtTaxPrice)).perform(click())
    }

    @Test
    fun clickingAddItem_navigatesToItems() {
        onView(withId(R.id.txtAddItem)).perform(click())
    }
}
