package com.koinvois.generator

import android.os.Bundle
import android.view.LayoutInflater
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
            

            insets
        }
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
