package com.koinvois.generator.core.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

/**
 * Centralized currency formatting so invoice totals, item prices and
 * report figures render consistently regardless of the currency chosen
 * in Settings.
 */
class CurrencyFormatter @Inject constructor() {

    fun format(
        amount: Double,
        currencyCode: String,
        locale: Locale = Locale.getDefault()
    ): String {
        val formatter = NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(currencyCode)
        }
        return formatter.format(amount)
    }
}
