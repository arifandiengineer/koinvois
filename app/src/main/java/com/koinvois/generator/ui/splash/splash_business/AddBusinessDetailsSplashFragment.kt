package com.koinvois.generator.ui.splash.splash_business

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.koinvois.generator.ui.splash.BusinessInputView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.core.os.BundleCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.databinding.FragmentSplashBusinessDetailsAddBinding
import com.koinvois.generator.ui.splash.SplashMainViewModel
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AddBusinessDetailsSplashFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var binding: FragmentSplashBusinessDetailsAddBinding? = null
    private val viewModel: SplashMainViewModel by hiltNavGraphViewModels(R.id.splash_navigation_graph)

    private var currentStep = 1

    private val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (null != selectedImageUri) {
                        withContext(Dispatchers.Main) {
                            binding?.imgSelectedImage?.setImageURI(selectedImageUri)
                        }
                    }
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.let {
                    BundleCompat.getParcelable(it, "data", Bitmap::class.java)
                } as Bitmap
                binding?.imgSelectedImage?.setImageBitmap(imageBitmap)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBusinessDetailsAddBinding.inflate(inflater, container, false)
        showStep(1)
        setClickListeners()
        setupTextWatchers()
        return binding?.root
    }

    private fun showStep(step: Int) {
        currentStep = step
        binding?.apply {
            firstBusinessView.hide()
            secondBusinessView.hide()
            thirdBusinessView.hide()

            // Reset indicators
            txtStep1.setBackgroundResource(R.drawable.shape_rounded_12dp)
            txtStep1.backgroundTintList = android.content.res.ColorStateList.valueOf("#33FFFFFF".toColorInt())
            txtStep1.setTextColor(android.graphics.Color.WHITE)
            txtStep1.setTypeface(null, android.graphics.Typeface.NORMAL)
            
            txtStep2.setBackgroundResource(R.drawable.shape_rounded_12dp)
            txtStep2.backgroundTintList = android.content.res.ColorStateList.valueOf("#33FFFFFF".toColorInt())
            txtStep2.setTextColor(android.graphics.Color.WHITE)
            txtStep2.setTypeface(null, android.graphics.Typeface.NORMAL)

            txtStep3.setBackgroundResource(R.drawable.shape_rounded_12dp)
            txtStep3.backgroundTintList = android.content.res.ColorStateList.valueOf("#33FFFFFF".toColorInt())
            txtStep3.setTextColor(android.graphics.Color.WHITE)
            txtStep3.setTypeface(null, android.graphics.Typeface.NORMAL)

            when (step) {
                1 -> {
                    firstBusinessView.visible()
                    txtStep1.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE)
                    txtStep1.setTextColor("#1B5E20".toColorInt())
                    txtStep1.setTypeface(null, android.graphics.Typeface.BOLD)
                }
                2 -> {
                    secondBusinessView.visible()
                    txtStep2.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE)
                    txtStep2.setTextColor("#1B5E20".toColorInt())
                    txtStep2.setTypeface(null, android.graphics.Typeface.BOLD)
                }
                3 -> {
                    thirdBusinessView.visible()
                    txtStep3.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE)
                    txtStep3.setTextColor("#1B5E20".toColorInt())
                    txtStep3.setTypeface(null, android.graphics.Typeface.BOLD)
                }
            }
        }
    }

    private fun setClickListeners() {
        binding?.apply {
            btnBackGlobal.setSafeOnClickListener {
                if (currentStep > 1) {
                    showStep(currentStep - 1)
                } else {
                    findNavController().navigateUp()
                }
            }

            btnForward1.setSafeOnClickListener {
                if (validateStep1()) {
                    showStep(2)
                }
            }

            btnForward2.setSafeOnClickListener { showStep(3) }
            
            btnChoseImage.setSafeOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                singleImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
            }

            btnTakePhoto.setSafeOnClickListener {
                cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }

            btnCreateInvoice.setSafeOnClickListener {
                if (validateStep1()) {
                    navigateToMain()
                } else {
                    showStep(1)
                }
            }
        }
    }

    private fun validateStep1(): Boolean {
        binding?.let {
            val name = it.editBusinessName.getText().trim()
            val email = it.editBusinessEmail.getText().trim()
            val phone = it.editBusinessPhone.getText().trim()
            val address1 = it.editBusinessAddressLine1.getText().trim()

            var isValid = true

            if (name.isEmpty()) {
                it.editBusinessName.setError("Nama bisnis wajib diisi")
                isValid = false
            } else {
                it.editBusinessName.setError(null)
            }

            if (email.isEmpty()) {
                it.editBusinessEmail.setError("Email wajib diisi")
                isValid = false
            } else {
                it.editBusinessEmail.setError(null)
            }

            if (phone.isEmpty()) {
                it.editBusinessPhone.setError("Nomor telepon wajib diisi")
                isValid = false
            } else {
                it.editBusinessPhone.setError(null)
            }

            if (address1.isEmpty()) {
                it.editBusinessAddressLine1.setError("Alamat (Baris 1) wajib diisi")
                isValid = false
            } else {
                it.editBusinessAddressLine1.setError(null)
            }

            // Reset errors for optional fields
            it.editBusinessAddressLine2.setError(null)
            it.editBusinessAddressLine3.setError(null)

            if (!isValid) {
                Toast.makeText(requireContext(), "Harap isi semua kolom yang wajib", Toast.LENGTH_SHORT).show()
            }

            return isValid
        }
        return false
    }

    private fun setupTextWatchers() {
        binding?.apply {
            editBusinessName.getEditText().doAfterTextChanged {
                txtPreviewName.text = if (it.isNullOrBlank()) getString(R.string.preview_business_name_placeholder) else it.toString()
            }
            editBusinessAddressLine1.getEditText().doAfterTextChanged {
                updatePreviewAddress()
            }
        }
    }

    private fun updatePreviewAddress() {
        binding?.apply {
            val addr1 = editBusinessAddressLine1.getText()
            txtPreviewAddress.text = if (addr1.isBlank()) getString(R.string.preview_business_address_placeholder) else addr1
        }
    }

    private fun navigateToMain() {
        try {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {

                val selectedBitmap = try {
                    binding?.imgSelectedImage?.drawable?.toBitmap()
                } catch (e: Exception) {
                    null
                }

                binding?.let {
                    with(it) {
                        var phoneNumber = editBusinessPhone.getText().trim()
                        if (phoneNumber.startsWith("+")) {
                            phoneNumber = phoneNumber.substring(1)
                        }
                        val businessPhoneNumber = phoneNumber.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                        val business = PersonalBusiness(
                            1,
                            selectedBitmap,
                            editBusinessName.getText(),
                            null,
                            null,
                            editBusinessAddressLine1.getText(),
                            editBusinessAddressLine2.getText(),
                            editBusinessAddressLine3.getText(),
                            editBusinessEmail.getText(),
                            businessPhoneNumber,
                            null,
                            null
                        )
                        viewModel.addBusiness(business)
                    }
                }

                appPreferences.setIsFirstTime(false)
                findNavController().navigate(
                    R.id.dashboard_navigation_graph,
                    null,
                    navOptions {
                        popUpTo(R.id.splash_navigation_graph) { inclusive = true }
                        launchSingleTop = true
                    }
                )
            }
        } catch (e: Exception) {
            Log.d("checkflow", e.toString())
            e.printStackTrace()
        }
    }
}
