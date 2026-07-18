package com.koinvois.generator.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.MainActivity
import com.koinvois.generator.ui.client.ClientMainActivity
import com.koinvois.generator.ui.home.HomeActivity
import com.koinvois.generator.ui.item.ItemMainActivity
import com.koinvois.generator.ui.lock.LockMainActivity
import com.koinvois.generator.ui.notifications.NotificationsActivity
import com.koinvois.generator.ui.setting.SettingActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Basic audit to ensure all top-level activities are launchable.
 */
@RunWith(AndroidJUnit4::class)
class ActivitiesAuditTest {

    @Test
    fun launchMainActivity() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Success if no crash
        }
    }

    @Test
    fun launchHomeActivity() {
        ActivityScenario.launch(HomeActivity::class.java).use { }
    }

    @Test
    fun launchClientMainActivity() {
        ActivityScenario.launch(ClientMainActivity::class.java).use { }
    }

    @Test
    fun launchItemMainActivity() {
        ActivityScenario.launch(ItemMainActivity::class.java).use { }
    }

    @Test
    fun launchSettingActivity() {
        ActivityScenario.launch(SettingActivity::class.java).use { }
    }

    @Test
    fun launchNotificationsActivity() {
        ActivityScenario.launch(NotificationsActivity::class.java).use { }
    }

    @Test
    fun launchLockMainActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), LockMainActivity::class.java).apply {
            putExtra("extra_pin_type", "new")
        }
        ActivityScenario.launch<LockMainActivity>(intent).use { }
    }
}
