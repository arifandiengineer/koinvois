package com.koinvois.generator.core.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Centralized currency formatting so invoice totals, item prices and report
 * figures render consistently regardless of the currency chosen in Settings.
 *
 * Holds the active currency code in memory (primed from DataStore at app
 * startup, see ApplicationClass) so every screen — Activities, Fragments,
 * plain RecyclerView adapters — can format money without touching DataStore
 * or Hilt at every call site. This mirrors how the currency setting needs to
 * take effect everywhere immediately: BaseActivity re-creates any screen that
 * was built with a stale currency code the moment it resumes (see
 * BaseActivity.onResume), so no full app restart is ever required.
 */
object CurrencyFormatter {

    @Volatile
    private var currencyCode: String = "USD"

    fun setCurrencyCode(code: String) {
        currencyCode = code
    }

    fun getCurrencyCode(): String = currencyCode

    fun format(amount: Double, locale: Locale = Locale.getDefault()): String {
        val formatter = NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(currencyCode)
        }
        return formatter.format(amount)
    }

    fun format(amount: Float, locale: Locale = Locale.getDefault()): String =
        format(amount.toDouble(), locale)
}
