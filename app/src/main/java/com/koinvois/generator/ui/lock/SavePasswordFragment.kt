package com.koinvois.generator.ui.lock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.databinding.FragmentSavePasswordBinding
import com.koinvois.generator.utilities.enums.PinTypeEnum
import com.koinvois.generator.utilities.extensions.getString
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SavePasswordFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var binding: FragmentSavePasswordBinding? = null
    private val arguments: SavePasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavePasswordBinding.inflate(inflater, container, false)

        setClickListener()

        return binding?.root
    }

    private fun setClickListener() {
        binding?.btnSavePassword?.setSafeOnClickListener {
            if ((binding?.editNewPassword?.getString()?.length ?: 0) != 4) {
                binding?.root?.showErrorSnackbar(getString(R.string.error_password_length))
            } else {
                if (binding?.editNewPassword?.getString() == binding?.editConfirmPassword?.getString()) {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        binding?.editNewPassword?.getString()?.let { pin ->
                            appPreferences.setPin(pin)
                            appPreferences.setLockMode(true)
                        }

                        when (arguments.pinType) {
                            PinTypeEnum.NEW.type -> {
                                val action =
                                    SavePasswordFragmentDirections.actionFragmentSavePasswordToFragmentSetting()
                                findNavController().navigate(action)
                            }
                            PinTypeEnum.RECOVERY.type -> {
                                findNavController().navigate(
                                    R.id.dashboard_navigation_graph,
                                    null,
                                    navOptions {
                                        popUpTo(R.id.splash_navigation_graph) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                )
                            }
                        }
                    }
                } else {
                    binding?.root?.showErrorSnackbar(getString(R.string.error_password_mismatch))
                }
            }
        }
    }
}