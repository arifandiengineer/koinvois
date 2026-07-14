package com.koinvois.generator.ui.lock

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.koinvois.generator.R
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
class LockMainFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var binding: LockMainFragmentBinding? = null
    private lateinit var viewModel: LockMainViewModel
    private val arguments: LockMainFragmentArgs by navArgs()
    private var secretAnswer: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LockMainFragmentBinding.inflate(inflater, container, false)

        activity?.let {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
            {
                secretAnswer = appPreferences.getSecurityAnswer().first()
                Log.e("ans", secretAnswer.toString())
                backPressed()
                setClickListeners()
            }
        }

        return binding?.root
    }

    private fun setClickListeners() {
        binding?.btnSaveSecurity?.setSafeOnClickListener {

            when (arguments.pinType) {
                PinTypeEnum.NEW.type -> {
                    if (binding?.editRecoverAnswer?.text?.isEmpty() == true) {
                        binding?.root?.showErrorSnackbar(getString(R.string.error_enter_nickname))
                    } else {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                            binding?.editRecoverAnswer?.getString()
                                ?.let { it2 -> appPreferences.setSecurityAnswer(it2) }

                            withContext(Dispatchers.Main) {
                                val action =
                                    LockMainFragmentDirections.actionFragmentLockMainToFragmentSavePassword(
                                        PinTypeEnum.NEW.type
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    }
                }

                PinTypeEnum.RECOVERY.type -> {

                    if (binding?.editRecoverAnswer?.text?.isEmpty() == true) {
                        binding?.root?.showErrorSnackbar(getString(R.string.error_enter_nickname))
                    } else if (binding?.editRecoverAnswer?.text.toString() == secretAnswer) {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                            binding?.editRecoverAnswer?.getString()
                                ?.let { it2 -> appPreferences.setSecurityAnswer(it2) }

                            withContext(Dispatchers.Main) {
                                val action =
                                    LockMainFragmentDirections.actionFragmentLockMainToFragmentSavePassword(
                                        PinTypeEnum.RECOVERY.type
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    } else {
                        binding?.root?.showErrorSnackbar(getString(R.string.error_enter_correct_nickname))
                    }
                }
            }
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            findNavController().navigateUp()
        }
    }


}