package com.koinvois.generator.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.core.data.preferences.ThemeMode
import com.koinvois.generator.databinding.SettingFragmentBinding
import com.koinvois.generator.utilities.enums.PinTypeEnum
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var binding: SettingFragmentBinding? = null
    private val viewModel: SettingViewModel by hiltNavGraphViewModels(R.id.setting_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingFragmentBinding.inflate(inflater, container, false)

        setUpToolbar()
        setClickListeners()


        return binding?.root
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.title_setting)
        binding?.customToolbar?.btnBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setSwitch() {
        var lockMode: Boolean
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default)
        {
            lockMode = appPreferences.getLockMode().first()
            withContext(Dispatchers.Main)
            {
                binding?.switchLock?.isChecked = lockMode
            }
        }

    }

    private fun setThemeValue() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val themeMode = appPreferences.getThemeMode().first()
            withContext(Dispatchers.Main) {
                binding?.switchDarkMode?.isChecked = themeMode == ThemeMode.DARK
            }
        }
    }


    private fun setClickListeners() {
        binding?.txtPersonalBusiness?.setSafeOnClickListener {
            val action = SettingFragmentDirections.actionSettingToAddBusiness()
            findNavController().navigate(action)
        }

        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            findNavController().navigateUp()
        }

        binding?.switchLock?.setOnCheckedChangeListener { switch, isChecked ->
            if (binding?.switchLock?.isPressed == true) {
                if (isChecked) {
                    val action =
                        SettingFragmentDirections.actionFragmentSettingToFragmentLockMainFragment(
                            PinTypeEnum.NEW.type
                        )
                    findNavController().navigate(action)
                } else {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default)
                    {
                        appPreferences.setLockMode(false)
                    }
                }
            }
        }

        binding?.switchDarkMode?.setOnCheckedChangeListener { _, isChecked ->
            if (binding?.switchDarkMode?.isPressed == true) {
                val selectedMode = if (isChecked) ThemeMode.DARK else ThemeMode.LIGHT
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    appPreferences.setThemeMode(selectedMode)
                }
                AppCompatDelegate.setDefaultNightMode(ThemeMode.toNightMode(selectedMode))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSwitch()
        setThemeValue()
    }


}
