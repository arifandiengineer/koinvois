package com.koinvois.generator.ui.invoices

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.SignatureActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignatureFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignatureActivity::class.java)

    @Test
    fun signatureActivity_displaysUI() {
        onView(withId(R.id.signatureView)).check(matches(isDisplayed()))
        onView(withId(R.id.btnClear)).check(matches(isDisplayed()))
        onView(withId(R.id.btnOk)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingClear_works() {
        onView(withId(R.id.btnClear)).perform(click())
    }
}
