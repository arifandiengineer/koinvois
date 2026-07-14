package com.koinvois.generator.ui.lock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.andrognito.pinlockview.PinLockListener
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
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
class EnterPasswordActivity : BaseActivity<FragmentEnterPasswordBinding>() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var savedPin = "10000"

    private val forgotPasswordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }

    override fun inflateBinding(): FragmentEnterPasswordBinding =
        FragmentEnterPasswordBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        lifecycleScope.launch(Dispatchers.Main) {
            savedPin = appPreferences.getPin().first()
            setLocker()
            setClickListeners()
        }
    }

    private fun setLocker() {
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        binding.pinLockView.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String) {
                Log.e("pin", pin)
                Log.e("pin", savedPin)

                if (pin == savedPin) {
                    correctPin()
                } else {
                    wrongPin()
                }
            }

            override fun onEmpty() {}

            override fun onPinChange(pinLength: Int, intermediatePin: String) {
                Log.e("pin", "changed")
            }
        })
    }

    private fun wrongPin() {
        binding.txtEnterPassword.text = getString(R.string.error_incorrect_pin)
        binding.pinLockView.resetPinLockView()
    }

    private fun setClickListeners() {
        binding.txtForgotPassword.setSafeOnClickListener {
            forgotPasswordLauncher.launch(LockMainActivity.newIntent(this, PinTypeEnum.RECOVERY.type))
        }
    }

    private fun correctPin() {
        binding.pinLockView.resetPinLockView()
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EnterPasswordActivity::class.java)
    }
}
