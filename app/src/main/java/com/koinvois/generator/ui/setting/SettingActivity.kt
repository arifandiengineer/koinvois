package com.koinvois.generator.ui.setting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.core.data.preferences.ThemeMode
import com.koinvois.generator.databinding.SettingFragmentBinding
import com.koinvois.generator.ui.lock.LockMainActivity
import com.koinvois.generator.ui.setting.add_business.AddBusinessDetailsActivity
import com.koinvois.generator.utilities.enums.PinTypeEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseActivity<SettingFragmentBinding>() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private val viewModel: SettingViewModel by viewModels()

    override fun inflateBinding(): SettingFragmentBinding =
        SettingFragmentBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        setSwitch()
        setThemeValue()
    }

    private fun setUpToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_setting)
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setSwitch() {
        lifecycleScope.launch(Dispatchers.Default) {
            val lockMode = appPreferences.getLockMode().first()
            withContext(Dispatchers.Main) {
                binding.switchLock.isChecked = lockMode
            }
        }
    }

    private fun setThemeValue() {
        lifecycleScope.launch(Dispatchers.Default) {
            val themeMode = appPreferences.getThemeMode().first()
            withContext(Dispatchers.Main) {
                binding.switchDarkMode.isChecked = themeMode == ThemeMode.DARK
            }
        }
    }

    private fun setClickListeners() {
        binding.txtPersonalBusiness.setSafeOnClickListener {
            startActivity(AddBusinessDetailsActivity.newIntent(this))
        }

        binding.customToolbar.btnBack.setSafeOnClickListener {
            finish()
        }

        binding.switchLock.setOnCheckedChangeListener { _, isChecked ->
            if (binding.switchLock.isPressed) {
                if (isChecked) {
                    startActivity(LockMainActivity.newIntent(this, PinTypeEnum.NEW.type))
                } else {
                    lifecycleScope.launch(Dispatchers.Default) {
                        appPreferences.setLockMode(false)
                    }
                }
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (binding.switchDarkMode.isPressed) {
                val selectedMode = if (isChecked) ThemeMode.DARK else ThemeMode.LIGHT
                lifecycleScope.launch(Dispatchers.Default) {
                    appPreferences.setThemeMode(selectedMode)
                }
                AppCompatDelegate.setDefaultNightMode(ThemeMode.toNightMode(selectedMode))
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingActivity::class.java)
    }
}
