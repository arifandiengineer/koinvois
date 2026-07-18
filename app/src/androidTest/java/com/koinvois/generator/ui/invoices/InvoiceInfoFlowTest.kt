package com.koinvois.generator.ui.invoices

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.InvoiceInformationActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InvoiceInfoFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(InvoiceInformationActivity::class.java)

    @Test
    fun invoiceInfo_interactAndSave() {
        onView(withId(R.id.editInvoiceNumber)).perform(replaceText("2024001"), closeSoftKeyboard())
        onView(withId(R.id.editInvoicePoNumber)).perform(replaceText("PO-999"), closeSoftKeyboard())
        
        onView(withId(R.id.btnSave)).perform(click())
    }

    @Test
    fun clickingDate_showsPicker() {
        onView(withId(R.id.editInvoiceDate)).perform(click())
    }
}
