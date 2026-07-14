package com.koinvois.generator.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * Centralized date formatting for invoice/estimate dates, due dates and
 * report ranges. minSdk 26 has java.time natively, no desugaring needed.
 */
class DateFormatter @Inject constructor() {

    fun format(
        date: LocalDate,
        pattern: String = DEFAULT_DATE_PATTERN,
        locale: Locale = Locale.getDefault()
    ): String = date.format(DateTimeFormatter.ofPattern(pattern, locale))

    fun format(
        dateTime: LocalDateTime,
        pattern: String = DEFAULT_DATE_TIME_PATTERN,
        locale: Locale = Locale.getDefault()
    ): String = dateTime.format(DateTimeFormatter.ofPattern(pattern, locale))

    fun today(pattern: String = DEFAULT_DATE_PATTERN): String = format(LocalDate.now(), pattern)

    companion object {
        const val DEFAULT_DATE_PATTERN = "dd MMM yyyy"
        const val DEFAULT_DATE_TIME_PATTERN = "dd MMM yyyy, HH:mm"
    }
}
