package com.koinvois.generator.ui.lock

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.andrognito.pinlockview.PinLockListener
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.databinding.FragmentEnterPasswordBinding
import com.koinvois.generator.utilities.enums.PinTypeEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EnterPasswordFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var binding: FragmentEnterPasswordBinding? = null
    private var savedPin = "10000"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
        {
            savedPin = appPreferences.getPin().first()
            setLocker()
            setClickListeners()
        }

        return binding?.root
    }

    private fun setLocker() {
        binding?.pinLockView?.attachIndicatorDots(binding?.indicatorDots)
        binding?.pinLockView?.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String) {
                Log.e("pin", pin)
                Log.e("pin", savedPin)

                if (pin == savedPin) {
                    correctPin()
                } else {
                    wrongPin()
                }
            }

            override fun onEmpty() {

            }

            override fun onPinChange(pinLength: Int, intermediatePin: String) {
                Log.e("pin", "changed")

            }
        })
    }

    private fun wrongPin() {
        binding?.txtEnterPassword?.text = getString(R.string.error_incorrect_pin)
        binding?.pinLockView?.resetPinLockView()
    }

    private fun setClickListeners() {
        binding?.txtForgotPassword?.setSafeOnClickListener {
            val action =
                EnterPasswordFragmentDirections.actionFragmentEnterPasswordToFragmentLockMainFragment(
                    PinTypeEnum.RECOVERY.type
                )
            findNavController().navigate(action)
        }
    }

    private fun correctPin() {
        binding?.pinLockView?.resetPinLockView()
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