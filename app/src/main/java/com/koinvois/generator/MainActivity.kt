package com.koinvois.generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.ActivityMainBinding
import com.koinvois.generator.ui.setting.SettingActivity
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    // Cached so destination changes (which don't redeliver insets on their
    // own) can still re-pad the content area when it toggles full-screen.
    private var latestSystemBarInsets: Insets? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.splash_navigation_graph)
        }
    }

    override fun inflateBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(LayoutInflater.from(this))

    override fun applyDefaultSystemBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            latestSystemBarInsets = bars

            // 1. Root handles NO bottom padding to let BottomNav background reach the edge
            binding.root.updatePadding(bottom = 0)

            // 2. Apply bottom padding to BottomNav for icon safety
            binding.navView.setPadding(0, 0, 0, bars.bottom)

            // 3. Header items handle Status Bar
            binding.viewHeader.setPadding(
                binding.viewHeader.paddingLeft,
                bars.top,
                binding.viewHeader.paddingRight,
                binding.viewHeader.paddingBottom
            )

            // 4. When the header/bottom nav are hidden (splash/onboarding), the
            // NavHostFragment itself expands edge-to-edge — pad it directly so
            // its content doesn't draw under the status/nav bar.
            applyContentAreaPadding()

            insets
        }
    }

    /**
     * Header/BottomNav already reserve top/bottom space for the system bars
     * while visible, so the content area needs no padding of its own then.
     * The moment they're hidden for a full-screen destination, the
     * NavHostFragment fills the whole screen and must absorb that padding
     * itself instead — otherwise its content (e.g. SplashMainFragment,
     * AddBusinessDetailsSplashFragment) would render under the status/nav bar.
     */
    private fun applyContentAreaPadding() {
        val bars = latestSystemBarInsets ?: return
        val isFullScreen = binding.headerContainer.visibility == View.GONE
        val navHostView = findViewById<View>(R.id.nav_host_fragment_activity_main)
        navHostView.updatePadding(
            top = if (isFullScreen) bars.top else 0,
            bottom = if (isFullScreen) bars.bottom else 0
        )
    }

    override fun setupView() {
        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            when (destination.id) {
                R.id.fragmentSplashMain,
                R.id.fragmentAddBusinessSplash -> {
                    binding.navView.hide()
                    binding.headerContainer.hide()
                    windowInsetsController.isAppearanceLightStatusBars = true
                }
                else -> {
                    binding.navView.visible()
                    binding.headerContainer.visible()
                    windowInsetsController.isAppearanceLightStatusBars = false
                }
            }
            applyContentAreaPadding()

            val parentGraphId = destination.parent?.id
            binding.txtMainTitle.text = when (parentGraphId) {
                R.id.dashboard_navigation_graph -> getString(R.string.title_dashboard)
                R.id.invoice_navigation_graph -> getString(R.string.title_invoices)
                R.id.estimate_navigation_graph -> getString(R.string.title_estimates)
                R.id.report_navigation_graph -> getString(R.string.title_report)
                R.id.more_navigation_graph -> getString(R.string.title_more)
                else -> {
                    when (destination.id) {
                        R.id.dashboard_navigation_graph -> getString(R.string.title_dashboard)
                        R.id.invoice_navigation_graph -> getString(R.string.title_invoices)
                        R.id.estimate_navigation_graph -> getString(R.string.title_estimates)
                        R.id.report_navigation_graph -> getString(R.string.title_report)
                        R.id.more_navigation_graph -> getString(R.string.title_more)
                        else -> destination.label
                    }
                }
            }
        }
        
        setClickListeners(navController)
    }

    private fun setClickListeners(navController: NavController) {
        binding.imgSetting.setSafeOnClickListener {
            startActivity(SettingActivity.newIntent(this))
        }
    }
}
