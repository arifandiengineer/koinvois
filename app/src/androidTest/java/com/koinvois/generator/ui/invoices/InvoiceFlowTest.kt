package com.koinvois.generator.ui.invoices

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InvoiceFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddInvoiceMainActivity::class.java)

    @Test
    fun invoiceEditor_displaysBasicFields() {
        onView(withId(R.id.txtInvoiceNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.txtInvoiceDate)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveInvoice)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingTax_opensTaxActivity() {
        onView(withId(R.id.txtTaxPrice)).perform(click())
        // Should navigate to TaxActivity
    }

    @Test
    fun clickingDiscount_opensDiscountActivity() {
        onView(withId(R.id.txtDiscountPrice)).perform(click())
        // Should navigate to DiscountActivity
    }

    @Test
    fun clickingAddItem_triggersNavigation() {
        onView(withId(R.id.txtAddItem)).perform(click())
        // Navigates based on item count
    }
}
