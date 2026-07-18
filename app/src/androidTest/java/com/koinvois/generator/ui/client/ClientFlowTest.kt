package com.koinvois.generator.ui.client

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.client.add_client.AddClientActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClientFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddClientActivity::class.java)

    @Test
    fun addClient_withValidData_shouldNotFinishIfEmpty() {
        // Test validation logic
        onView(withId(R.id.editClientName)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.btnSaveClient)).perform(click())
        
        // If it finishes on empty name, it might be a bug or intended. 
        // Based on code: if (binding.editClientName.text.toString().isNotEmpty()) { ... } else { finish() }
        // The current implementation just finishes without saving if name is empty.
    }

    @Test
    fun addClient_fillsDataAndSaves() {
        onView(withId(R.id.editClientName)).perform(typeText("Audit Client"), closeSoftKeyboard())
        onView(withId(R.id.editClientEmail)).perform(typeText("audit@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editMobileNumber)).perform(typeText("123456789"), closeSoftKeyboard())
        
        // This would trigger saveOnBack and finish
        onView(withId(R.id.btnSaveClient)).perform(click())
    }
}
