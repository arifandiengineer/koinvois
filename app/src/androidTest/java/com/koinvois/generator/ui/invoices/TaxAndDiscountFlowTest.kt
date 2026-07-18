package com.koinvois.generator.ui.invoices

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.TaxActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.DiscountActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaxAndDiscountFlowTest {

    @get:Rule
    val taxActivityRule = ActivityScenarioRule(TaxActivity::class.java)

    @Test
    fun taxActivity_interactAndSave() {
        onView(withId(R.id.editTaxLabel)).perform(typeText("VAT"), closeSoftKeyboard())
        onView(withId(R.id.editRate)).perform(typeText("10"), closeSoftKeyboard())
        onView(withId(R.id.customToolbar)).perform(click())
    }

    @Test
    fun discountActivity_interactAndSave() {
        androidx.test.core.app.ActivityScenario.launch(DiscountActivity::class.java).use {
            onView(withId(R.id.editDiscountAmount)).perform(typeText("5000"), closeSoftKeyboard())
            onView(withId(R.id.customToolbar)).perform(click())
        }
    }
}
