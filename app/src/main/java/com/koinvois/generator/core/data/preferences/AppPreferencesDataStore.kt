package com.koinvois.generator.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Hilt-injectable replacement for the old `DataStore` singleton object.
 * Preserves every existing key, default value and quirk (e.g. invoice/estimate
 * number getters returning stored+1) so behavior is unchanged for callers.
 */
@Singleton
class AppPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    private val LOCK_MODE = booleanPreferencesKey("lock_mode")
    private val SECURITY_ANSWER = stringPreferencesKey("security_answer")
    private val PIN = stringPreferencesKey("pin")
    private val INVOICE_NUMBER = intPreferencesKey("invoice_number")
    private val ESTIMATE_NUMBER = intPreferencesKey("estimate_number")
    private val THEME_MODE = stringPreferencesKey("theme_mode")
    private val CURRENCY_CODE = stringPreferencesKey("currency_code")

    suspend fun setIsFirstTime(isFirstTime: Boolean) {
        context.dataStore.edit { setting ->
            setting[IS_FIRST_TIME] = isFirstTime
        }
    }

    fun getIsFirstTime(): Flow<Boolean> {
        return context.dataStore.data.map { setting ->
            setting[IS_FIRST_TIME] ?: true
        }
    }

    suspend fun setLockMode(locked: Boolean) {
        context.dataStore.edit { setting ->
            setting[LOCK_MODE] = locked
        }
    }

    fun getLockMode(): Flow<Boolean> {
        return context.dataStore.data.map { setting ->
            setting[LOCK_MODE] ?: false
        }
    }

    suspend fun setSecurityAnswer(securityAnswer: String) {
        context.dataStore.edit { setting ->
            setting[SECURITY_ANSWER] = securityAnswer
        }
    }

    fun getSecurityAnswer(): Flow<String> {
        return context.dataStore.data.map { setting ->
            setting[SECURITY_ANSWER] ?: "Empty"
        }
    }

    suspend fun setPin(pin: String) {
        context.dataStore.edit { setting ->
            setting[PIN] = pin
        }
    }

    fun getPin(): Flow<String> {
        return context.dataStore.data.map { setting ->
            setting[PIN] ?: "10000"
        }
    }

    suspend fun setInvoiceNumber(invoiceNumber: Int) {
        context.dataStore.edit { setting ->
            setting[INVOICE_NUMBER] = invoiceNumber
        }
    }

    fun getInvoiceNumber(): Flow<Int> {
        return context.dataStore.data.map { setting ->
            setting[INVOICE_NUMBER]?.plus(1) ?: 1
        }
    }

    suspend fun setEstimateNumber(estimateNumber: Int) {
        context.dataStore.edit { setting ->
            setting[ESTIMATE_NUMBER] = estimateNumber
        }
    }

    fun getEstimateNumber(): Flow<Int> {
        return context.dataStore.data.map { setting ->
            setting[ESTIMATE_NUMBER]?.plus(1) ?: 1
        }
    }

    suspend fun setThemeMode(themeMode: String) {
        context.dataStore.edit { setting ->
            setting[THEME_MODE] = themeMode
        }
    }

    fun getThemeMode(): Flow<String> {
        return context.dataStore.data.map { setting ->
            setting[THEME_MODE] ?: "system"
        }
    }

    suspend fun setCurrencyCode(currencyCode: String) {
        context.dataStore.edit { setting ->
            setting[CURRENCY_CODE] = currencyCode
        }
    }

    fun getCurrencyCode(): Flow<String> {
        return context.dataStore.data.map { setting ->
            setting[CURRENCY_CODE] ?: "USD"
        }
    }
}