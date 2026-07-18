package com.koinvois.generator.ui.invoices

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.AddPhotoToInvoiceActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddPhotoToInvoiceActivity::class.java)

    @Test
    fun photoActivity_interactAndSave() {
        onView(withId(R.id.editPhotoDescription)).perform(typeText("Audit Photo"), closeSoftKeyboard())
        onView(withId(R.id.editAdditionalDetails)).perform(typeText("Audit Photo Details"), closeSoftKeyboard())
        
        onView(withId(R.id.btnSave)).perform(click())
    }

    @Test
    fun clickingPhoto_triggersSelector() {
        onView(withId(R.id.imgPhoto)).perform(click())
    }
}
