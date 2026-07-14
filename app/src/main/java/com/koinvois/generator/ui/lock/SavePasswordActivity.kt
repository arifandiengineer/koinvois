package com.koinvois.generator.ui.lock

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.databinding.ActivityPasswordSaveBinding
import com.koinvois.generator.ui.setting.SettingActivity
import com.koinvois.generator.utilities.enums.PinTypeEnum
import com.koinvois.generator.utilities.extensions.getString
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SavePasswordActivity : BaseActivity<ActivityPasswordSaveBinding>() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private val pinType: String by lazy { intent.getStringExtra(EXTRA_PIN_TYPE) ?: PinTypeEnum.NEW.type }

    override fun inflateBinding(): ActivityPasswordSaveBinding =
        ActivityPasswordSaveBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnSavePassword.setSafeOnClickListener {
            if ((binding.editNewPassword.getString()?.length ?: 0) != 4) {
                binding.root.showErrorSnackbar(getString(R.string.error_password_length))
            } else {
                if (binding.editNewPassword.getString() == binding.editConfirmPassword.getString()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.editNewPassword.getString()?.let { pin ->
                            appPreferences.setPin(pin)
                            appPreferences.setLockMode(true)
                        }

                        when (pinType) {
                            PinTypeEnum.NEW.type -> {
                                startActivity(
                                    Intent(this@SavePasswordActivity, SettingActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    }
                                )
                                finish()
                            }
                            PinTypeEnum.RECOVERY.type -> {
                                setResult(RESULT_OK)
                                finish()
                            }
                        }
                    }
                } else {
                    binding.root.showErrorSnackbar(getString(R.string.error_password_mismatch))
                }
            }
        }
    }

    companion object {
        private const val EXTRA_PIN_TYPE = "extra_pin_type"

        fun newIntent(context: Context, pinType: String): Intent =
            Intent(context, SavePasswordActivity::class.java).apply {
                putExtra(EXTRA_PIN_TYPE, pinType)
            }
    }
}
