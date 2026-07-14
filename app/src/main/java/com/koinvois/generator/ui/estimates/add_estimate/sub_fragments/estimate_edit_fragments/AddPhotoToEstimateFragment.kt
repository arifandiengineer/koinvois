package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.koinvois.generator.R
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.database.models.EstimatePhoto
import com.koinvois.generator.databinding.FragmentAddPhotoToEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.getString
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPhotoToEstimateFragment : Fragment() {

    private var binding: FragmentAddPhotoToEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)
    private var imagePicked: Boolean = false
    private val argument: AddPhotoToEstimateFragmentArgs by navArgs()

    private val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        val path = getPathFromURI(selectedImageUri)
                        withContext(Dispatchers.Main) {
                            binding?.imgPhoto?.layoutParams?.height = 500
                            binding?.imgPhoto?.layoutParams?.width = 500
                            binding?.imgPhoto?.requestLayout()

                            binding?.imgPhoto?.setImageURI(selectedImageUri)
                            imagePicked = true
                        }
                    }
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                binding?.imgPhoto?.layoutParams?.height = 500
                binding?.imgPhoto?.layoutParams?.width = 500
                binding?.imgPhoto?.requestLayout()

                binding?.imgPhoto?.setImageBitmap(imageBitmap)
                imagePicked = true
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPhotoToEstimateBinding.inflate(inflater, container, false)

        activity?.let {
            setUpToolbar()
            backPressed()
            loadView()
        }

        return binding?.root
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.apply {
            btnBack.visible()
            txtToolbarTitle.text = getString(R.string.title_photo)

            imgSecondaryAction.apply {
                if (argument.photoType == DBEnum.OLD.entryType) {
                    visible()
                    setImageResource(R.drawable.btn_delete)
                    setSafeOnClickListener {
                        BaseDialog.confirm(
                            context = requireContext(),
                            title = getString(R.string.delete_confirm_title),
                            message = getString(R.string.delete_photo_confirm_message),
                            positiveText = getString(R.string.delete_confirm_positive),
                            negativeText = getString(R.string.delete_confirm_negative),
                            onConfirm = {
                                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
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
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    saveOnBack()
                }
            }
        }
    }

    private fun setClickListeners(activity: Context) {

        binding?.imgPhoto?.setSafeOnClickListener {
            createBottomSheetDialog(activity)
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                saveOnBack()
            }
        }
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

    private suspend fun deletePhoto() {
        viewModel.currentSelectedPhoto?.let {
            viewModel.deleteEstimatePhoto(it)
            viewModel.photosForEstimate?.remove(it)
        }
        viewModel.currentSelectedPhoto = null
        findNavController().navigateUp()
    }

    private fun loadView() {
        viewModel.currentSelectedPhoto?.let { photo ->
            binding?.let { view ->
                with(view)
                {
                    photo.estimatePhotoDescription?.let {
                        editPhotoDescription.setText(it)
                    }

                    photo.estimatePhotoAdditionalDetails?.let {
                        editAdditionalDetails.setText(it)
                    }

                    photo.estimatePhoto?.let {
                        binding?.imgPhoto?.layoutParams?.height = 500
                        binding?.imgPhoto?.layoutParams?.width = 500
                        binding?.imgPhoto?.requestLayout()
                        imagePicked = true
                        imgPhoto.setImageBitmap(it)
                    }
                }
            }
        }
    }

    private fun getPathFromURI(uri: Uri?): String {
        val contentResolver: ContentResolver? = activity?.contentResolver
        var path = ""
        if (contentResolver != null) {
            val cursor = uri?.let { contentResolver.query(it, null, null, null, null) }
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private suspend fun saveOnBack() {
        if (imagePicked) {
            if (binding?.editPhotoDescription?.text?.isNotEmpty() == true) {

                val estimatePhoto = EstimatePhoto(
                    viewModel.currentSelectedPhoto?.estimatePhotoId,
                    viewModel.estimatePrimaryId?.toInt(),
                    binding?.imgPhoto?.drawable?.toBitmap(),
                    binding?.editPhotoDescription?.getString(),
                    binding?.editAdditionalDetails?.getString()
                )

                when (argument.photoType) {
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
                    binding?.imgPhoto?.drawable?.toBitmap(),
                    "Photo #" + viewModel.photosForEstimate?.size?.plus(1).toString(),
                    binding?.editAdditionalDetails?.getString()
                )

                when (argument.photoType) {
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
            Toast.makeText(activity, getString(R.string.error_choose_photo), Toast.LENGTH_SHORT).show()
        }
        viewModel.currentSelectedPhoto = null
        findNavController().navigateUp()
    }

}