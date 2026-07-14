package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.BundleCompat
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.database.models.EstimatePhoto
import com.koinvois.generator.databinding.ActivityEstimateAddPhotoBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.getString
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddPhotoToEstimateActivity : BaseActivity<ActivityEstimateAddPhotoBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()
    private var imagePicked: Boolean = false
    private val photoType: String by lazy { intent.getStringExtra(EXTRA_PHOTO_TYPE) ?: DBEnum.NEW.entryType }

    private val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch(Dispatchers.Default) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (null != selectedImageUri) {
                        val path = getPathFromURI(selectedImageUri)
                        withContext(Dispatchers.Main) {
                            binding.imgPhoto.layoutParams?.height = 500
                            binding.imgPhoto.layoutParams?.width = 500
                            binding.imgPhoto.requestLayout()

                            binding.imgPhoto.setImageURI(selectedImageUri)
                            imagePicked = true
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
                binding.imgPhoto.layoutParams?.height = 500
                binding.imgPhoto.layoutParams?.width = 500
                binding.imgPhoto.requestLayout()

                binding.imgPhoto.setImageBitmap(imageBitmap)
                imagePicked = true
            }
        }

    override fun inflateBinding(): ActivityEstimateAddPhotoBinding =
        ActivityEstimateAddPhotoBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        onBackPressedDispatcher.addCallback(this) {
            lifecycleScope.launch(Dispatchers.Main) { saveOnBack() }
        }
        setClickListeners()
        loadView()
    }

    private fun setClickListeners() {
        binding.imgPhoto.setSafeOnClickListener {
            createBottomSheetDialog(this)
        }
    }

    private suspend fun deletePhoto() {
        viewModel.currentSelectedPhoto?.let {
            viewModel.deleteEstimatePhoto(it)
            viewModel.photosForEstimate?.remove(it)
        }
        viewModel.currentSelectedPhoto = null
        finish()
    }

    private suspend fun saveOnBack() {
        if (imagePicked) {
            if (binding.editPhotoDescription.text?.isNotEmpty() == true) {

                val estimatePhoto = EstimatePhoto(
                    viewModel.currentSelectedPhoto?.estimatePhotoId,
                    viewModel.estimatePrimaryId?.toInt(),
                    binding.imgPhoto.drawable?.toBitmap(),
                    binding.editPhotoDescription.getString(),
                    binding.editAdditionalDetails.getString()
                )

                when (photoType) {
                    DBEnum.NEW.entryType -> {
                        viewModel.insertEstimatePhoto(estimatePhoto)
                        viewModel.photosForEstimate?.add(estimatePhoto)
                    }
                    DBEnum.OLD.entryType -> {
                        viewModel.updateEstimatePhoto(estimatePhoto)

                        viewModel.photosForEstimate?.indexOf(viewModel.currentSelectedPhoto)
                            ?.let { index ->
                                viewModel.photosForEstimate?.remove(viewModel.currentSelectedPhoto)
                                viewModel.photosForEstimate?.add(index, estimatePhoto)
                            }
                    }
                }

            } else {

                val estimatePhoto = EstimatePhoto(
                    viewModel.currentSelectedPhoto?.estimatePhotoId,
                    viewModel.estimatePrimaryId?.toInt(),
                    binding.imgPhoto.drawable?.toBitmap(),
                    "Photo #" + viewModel.photosForEstimate?.size?.plus(1).toString(),
                    binding.editAdditionalDetails.getString()
                )

                when (photoType) {
                    DBEnum.NEW.entryType -> {
                        viewModel.insertEstimatePhoto(estimatePhoto)
                        viewModel.photosForEstimate?.add(estimatePhoto)
                    }
                    DBEnum.OLD.entryType -> {
                        viewModel.updateEstimatePhoto(estimatePhoto)

                        viewModel.photosForEstimate?.indexOf(viewModel.currentSelectedPhoto)
                            ?.let { index ->
                                viewModel.photosForEstimate?.remove(viewModel.currentSelectedPhoto)
                                viewModel.photosForEstimate?.add(index, estimatePhoto)
                            }
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.error_choose_photo), Toast.LENGTH_SHORT).show()
        }
        viewModel.currentSelectedPhoto = null
        finish()
    }

    private fun getPathFromURI(uri: Uri?): String {
        val contentResolver: ContentResolver = contentResolver
        var path = ""
        val cursor = uri?.let { contentResolver.query(it, null, null, null, null) }
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            path = cursor.getString(idx)
            cursor.close()
        }
        return path
    }

    private fun createBottomSheetDialog(context: Context) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
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

    private fun setUpToolbar() {
        binding.customToolbar.apply {
            btnBack.visible()
            txtToolbarTitle.text = getString(R.string.title_photo)

            imgSecondaryAction.apply {
                if (photoType == DBEnum.OLD.entryType) {
                    visible()
                    setImageResource(R.drawable.btn_delete)
                    setSafeOnClickListener {
                        BaseDialog.confirm(
                            context = this@AddPhotoToEstimateActivity,
                            title = getString(R.string.delete_confirm_title),
                            message = getString(R.string.delete_photo_confirm_message),
                            positiveText = getString(R.string.delete_confirm_positive),
                            negativeText = getString(R.string.delete_confirm_negative),
                            onConfirm = {
                                lifecycleScope.launch(Dispatchers.Main) {
                                    deletePhoto()
                                }
                            }
                        )
                    }
                } else {
                    inVisible()
                }
            }

            btnBack.setSafeOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    saveOnBack()
                }
            }
        }
    }

    private fun loadView() {
        viewModel.currentSelectedPhoto?.let { photo ->
            with(binding) {
                photo.estimatePhotoDescription?.let {
                    editPhotoDescription.setText(it)
                }

                photo.estimatePhotoAdditionalDetails?.let {
                    editAdditionalDetails.setText(it)
                }

                photo.estimatePhoto?.let {
                    imgPhoto.layoutParams?.height = 500
                    imgPhoto.layoutParams?.width = 500
                    imgPhoto.requestLayout()
                    imagePicked = true

                    imgPhoto.setImageBitmap(it)
                }
            }
        }
    }

    companion object {
        private const val EXTRA_PHOTO_TYPE = "extra_photo_type"

        fun newIntent(context: Context, photoType: String): Intent =
            Intent(context, AddPhotoToEstimateActivity::class.java).apply {
                putExtra(EXTRA_PHOTO_TYPE, photoType)
            }
    }
}
