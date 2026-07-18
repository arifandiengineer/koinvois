package com.koinvois.generator.ui.item

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.R
import com.koinvois.generator.ui.item.add_item.AddItemActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddItemActivity::class.java)

    @Test
    fun addItem_fillsDataAndSaves() {
        onView(withId(R.id.editItemDescription)).perform(typeText("Audit Product"), closeSoftKeyboard())
        onView(withId(R.id.editItemUnitCost)).perform(typeText("50000"), closeSoftKeyboard())
        onView(withId(R.id.editAdditionalItemDetails)).perform(typeText("Product Description"), closeSoftKeyboard())
        
        onView(withId(R.id.btnSaveItem)).perform(click())
    }
}
