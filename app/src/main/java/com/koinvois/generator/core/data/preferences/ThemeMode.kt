package com.koinvois.generator.core.data.preferences

import androidx.appcompat.app.AppCompatDelegate

/**
 * String values stored in [AppPreferencesDataStore.THEME_MODE], and the
 * mapping to [AppCompatDelegate] night-mode constants. Kept as plain
 * strings (not an enum) so they round-trip through DataStore unchanged.
 */
object ThemeMode {
    const val LIGHT = "light"
    const val DARK = "dark"
    const val SYSTEM = "system"

    fun toNightMode(mode: String): Int = when (mode) {
        LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        DARK -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}
