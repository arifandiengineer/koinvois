package com.koinvois.generator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.core.data.preferences.ThemeMode
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        // Material You: use the device wallpaper palette on Android 12+,
        // falls back to the static brand theme (themes.xml) below API 31.
        DynamicColors.applyToActivitiesIfAvailable(this)

        // Apply the persisted theme choice before any Activity is created,
        // so there's no light->dark flash on cold start.
        val themeMode = runBlocking { AppPreferencesDataStore(this@ApplicationClass).getThemeMode().first() }
        AppCompatDelegate.setDefaultNightMode(ThemeMode.toNightMode(themeMode))
    }
}