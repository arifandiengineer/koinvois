package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.BundleCompat
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.database.models.PersonalBusiness
import com.koinvois.generator.databinding.FragmentEditBusinessDetailsFromEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EditBusinessDetailsFromEstimateActivity : BaseActivity<FragmentEditBusinessDetailsFromEstimateBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()

    private val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch(Dispatchers.Default) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (null != selectedImageUri) {
                        withContext(Dispatchers.Main) {
                            binding.imgImagePicker.setImageURI(selectedImageUri)
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
                binding.imgImagePicker.setImageBitmap(imageBitmap)
            }
        }

    override fun inflateBinding(): FragmentEditBusinessDetailsFromEstimateBinding =
        FragmentEditBusinessDetailsFromEstimateBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getBusiness()
            setObserver()
            setClickListeners()
            onBackPressedDispatcher.addCallback(this@EditBusinessDetailsFromEstimateActivity) { saveOnBack() }
            setUpToolbar()
        }
    }

    private fun setUpToolbar() {
        with(binding) {
            customToolbar.imgRightAction.inVisible()
            customToolbar.btnBack.visible()
            customToolbar.imgSecondaryAction.inVisible()

            customToolbar.txtToolbarTitle.text = getString(R.string.title_business_details)
        }
    }

    private fun setObserver() {
        val pb = viewModel.businessUpdateModel
        with(binding) {
            if (pb?.businessLogo != null)
                imgImagePicker.setImageBitmap(pb.businessLogo)
            editBusinessName.setText(pb?.businessName)
            editBusinessOwnerName.setText(pb?.businessOwnerName)
            editBusinessNumber.setText(pb?.businessNumber)

            editAddress1.setText(pb?.businessAddress1)
            editAddress2.setText(pb?.businessAddress2)
            editAddress3.setText(pb?.businessAddress3)

            editEmail.setText(pb?.businessEmail)
            pb?.businessPhoneNumber?.let { it1 -> editPhoneNumber.setText(it1.toString()) }
            pb?.businessMobileNumber?.let { it1 -> editMobileNumber.setText(it1.toString()) }
            editWebsite.setText(pb?.businessWebsite)
        }
    }

    private fun saveOnBack() {
        lifecycleScope.launch(Dispatchers.IO) {
            with(binding) {
                val business = PersonalBusiness(
                    1,
                    imgImagePicker.drawable.toBitmap(),
                    editBusinessName.getString(),
                    editBusinessOwnerName.getString(),
                    editBusinessNumber.getString(),
                    editAddress1.getString(),
                    editAddress2.getString(),
                    editAddress3.getString(),
                    editEmail.getString(),
                    editPhoneNumber.getInt(),
                    editMobileNumber.getInt(),
                    editWebsite.getString()
                )

                viewModel.updateBusiness(business)
            }

            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

    private fun setClickListeners() {
        binding.imgImagePicker.setSafeOnClickListener {
            createBottomSheetDialog(this)
        }

        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.btnSave.setSafeOnClickListener {
            saveOnBack()
        }
    }

    private fun createBottomSheetDialog(context: Activity) {
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_logo)

        val btnCancel = bottomSheetDialog.findViewById<TextView>(R.id.txtCancel)
        val btnCamera = bottomSheetDialog.findViewById<TextView>(R.id.txtTakePhoto)
        val btnLibrary = bottomSheetDialog.findViewById<TextView>(R.id.txtChoseFromLibrary)

        btnCamera?.setSafeOnClickListener {
            bottomSheetDialog.dismiss()
            cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        btnLibrary?.setSafeOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            singleImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }

        btnCancel?.setSafeOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EditBusinessDetailsFromEstimateActivity::class.java)
    }
}
