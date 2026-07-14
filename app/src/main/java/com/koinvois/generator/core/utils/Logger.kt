package com.koinvois.generator.core.utils

import android.util.Log
import com.koinvois.generator.BuildConfig

/**
 * Debug-only logging so invoice/client data never reaches logcat in a
 * release build (this app stores financial data locally with no backend).
 */
object Logger {
    private const val DEFAULT_TAG = "Koinvois"

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.d(tag, message)
    }

    fun w(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.w(tag, message)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) Log.e(tag, message, throwable)
    }
}
