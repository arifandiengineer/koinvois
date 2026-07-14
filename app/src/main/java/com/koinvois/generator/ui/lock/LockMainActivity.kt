package com.koinvois.generator.ui.lock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.databinding.LockMainFragmentBinding
import com.koinvois.generator.utilities.enums.PinTypeEnum
import com.koinvois.generator.utilities.extensions.getString
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LockMainActivity : BaseActivity<LockMainFragmentBinding>() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private val pinType: String by lazy { intent.getStringExtra(EXTRA_PIN_TYPE) ?: PinTypeEnum.NEW.type }
    private var secretAnswer: String? = null

    private val savePasswordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode)
            finish()
        }

    override fun inflateBinding(): LockMainFragmentBinding =
        LockMainFragmentBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        lifecycleScope.launch(Dispatchers.Main) {
            secretAnswer = appPreferences.getSecurityAnswer().first()
            Log.e("ans", secretAnswer.toString())
            setClickListeners()
        }
    }

    private fun setClickListeners() {
        binding.btnSaveSecurity.setSafeOnClickListener {
            when (pinType) {
                PinTypeEnum.NEW.type -> {
                    if (binding.editRecoverAnswer.text?.isEmpty() == true) {
                        binding.root.showErrorSnackbar(getString(R.string.error_enter_nickname))
                    } else {
                        lifecycleScope.launch(Dispatchers.Default) {
                            binding.editRecoverAnswer.getString()
                                ?.let { answer -> appPreferences.setSecurityAnswer(answer) }

                            withContext(Dispatchers.Main) {
                                savePasswordLauncher.launch(
                                    SavePasswordActivity.newIntent(this@LockMainActivity, PinTypeEnum.NEW.type)
                                )
                            }
                        }
                    }
                }

                PinTypeEnum.RECOVERY.type -> {
                    if (binding.editRecoverAnswer.text?.isEmpty() == true) {
                        binding.root.showErrorSnackbar(getString(R.string.error_enter_nickname))
                    } else if (binding.editRecoverAnswer.text.toString() == secretAnswer) {
                        lifecycleScope.launch(Dispatchers.Default) {
                            binding.editRecoverAnswer.getString()
                                ?.let { answer -> appPreferences.setSecurityAnswer(answer) }

                            withContext(Dispatchers.Main) {
                                savePasswordLauncher.launch(
                                    SavePasswordActivity.newIntent(this@LockMainActivity, PinTypeEnum.RECOVERY.type)
                                )
                            }
                        }
                    } else {
                        binding.root.showErrorSnackbar(getString(R.string.error_enter_correct_nickname))
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_PIN_TYPE = "extra_pin_type"

        fun newIntent(context: Context, pinType: String): Intent =
            Intent(context, LockMainActivity::class.java).apply {
                putExtra(EXTRA_PIN_TYPE, pinType)
            }
    }
}
