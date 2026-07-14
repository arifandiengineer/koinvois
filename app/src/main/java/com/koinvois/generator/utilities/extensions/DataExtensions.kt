package com.koinvois.generator.utilities.extensions

import android.icu.text.SimpleDateFormat
import java.util.*

fun Date.toStringFormat(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}