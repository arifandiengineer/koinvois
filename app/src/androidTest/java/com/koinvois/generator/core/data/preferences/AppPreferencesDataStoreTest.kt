package com.koinvois.generator.core.data.preferences

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppPreferencesDataStoreTest {

    private lateinit var dataStore: AppPreferencesDataStore

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataStore = AppPreferencesDataStore(context)
    }

    @Test
    fun testPinStorage() = runBlocking {
        dataStore.setPin("9999")
        val pin = dataStore.getPin().first()
        assertEquals("9999", pin)
    }

    @Test
    fun testInvoiceNumberIncrement() = runBlocking {
        // Clear or set initial
        dataStore.setInvoiceNumber(10)
        // getInvoiceNumber returns stored value + 1
        val nextNumber = dataStore.getInvoiceNumber().first()
        assertEquals(11, nextNumber)
    }

    @Test
    fun testLockMode() = runBlocking {
        dataStore.setLockMode(true)
        val isLocked = dataStore.getLockMode().first()
        assertEquals(true, isLocked)
    }
}
