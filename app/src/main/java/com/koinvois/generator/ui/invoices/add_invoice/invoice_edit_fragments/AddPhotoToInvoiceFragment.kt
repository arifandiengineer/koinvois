package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

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
import com.koinvois.generator.database.models.InvoicePhoto
import com.koinvois.generator.databinding.FragmentAddPhotoToInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.getString
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPhotoToInvoiceFragment : Fragment() {

    private var binding: FragmentAddPhotoToInvoiceBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    private var imagePicked: Boolean = false
    private val argument: AddPhotoToInvoiceFragmentArgs by navArgs()

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
        binding = FragmentAddPhotoToInvoiceBinding.inflate(inflater, container, false)
        setUpToolbar()
        backPressed()
        activity?.let { setClickListeners(it) }
        loadView()
        return binding?.root
    }

    private fun setClickListeners(activity: Context) {

        binding?.imgPhoto?.setSafeOnClickListener {
            createBottomSheetDialog(activity)
        }
    }

    private suspend fun deletePhoto() {
        viewModel.currentSelectedPhoto?.let {
            viewModel.deleteInvoicePhoto(it)
            viewModel.photosForInvoice?.remove(it)
        }
        viewModel.currentSelectedPhoto = null
        findNavController().navigateUp()
    }

    private suspend fun saveOnBack() {
        if (imagePicked) {
            if (binding?.editPhotoDescription?.text?.isNotEmpty() == true) {

                val invoicePhoto = InvoicePhoto(
                    viewModel.currentSelectedPhoto?.invoicePhotoId,
                    viewModel.invoicePrimaryId?.toInt(),
                    binding?.imgPhoto?.drawable?.toBitmap(),
                    binding?.editPhotoDescription?.getString(),
                    binding?.editAdditionalDetails?.getString()
                )

                when (argument.photoType) {
                    DBEnum.NEW.entryType -> {

                        viewModel.insertInvoicePhoto(invoicePhoto)
                        viewModel.photosForInvoice?.add(invoicePhoto)
                    }
                    DBEnum.OLD.entryType -> {

                        viewModel.updateInvoicePhoto(invoicePhoto)

                        viewModel.photosForInvoice?.indexOf(viewModel.currentSelectedPhoto)
                            ?.let { index ->
                                viewModel.photosForInvoice?.remove(viewModel.currentSelectedPhoto)
                                viewModel.photosForInvoice?.add(index, invoicePhoto)
                            }
                    }
                }

            } else {

                val invoicePhoto = InvoicePhoto(
                    viewModel.currentSelectedPhoto?.invoicePhotoId,
                    viewModel.invoicePrimaryId?.toInt(),
                    binding?.imgPhoto?.drawable?.toBitmap(),
                    "Photo #" + viewModel.photosForInvoice?.size?.plus(1).toString(),
                    binding?.editAdditionalDetails?.getString()
                )

                when (argument.photoType) {
                    DBEnum.NEW.entryType -> {

                        viewModel.insertInvoicePhoto(invoicePhoto)
                        viewModel.photosForInvoice?.add(invoicePhoto)
                    }
                    DBEnum.OLD.entryType -> {

                        viewModel.updateInvoicePhoto(invoicePhoto)

                        viewModel.photosForInvoice?.indexOf(viewModel.currentSelectedPhoto)
                            ?.let { index ->
                                viewModel.photosForInvoice?.remove(viewModel.currentSelectedPhoto)
                                viewModel.photosForInvoice?.add(index, invoicePhoto)
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

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                saveOnBack()
            }
        }
    }

    private fun loadView() {
        viewModel.currentSelectedPhoto?.let { photo ->
            binding?.let { view ->
                with(view)
                {
                    photo.invoicePhotoDescription?.let {
                        editPhotoDescription.setText(it)
                    }

                    photo.invoicePhotoAdditionalDetails?.let {
                        editAdditionalDetails.setText(it)
                    }

                    photo.invoicePhoto?.let {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}