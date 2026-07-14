package com.koinvois.generator.ui.setting.add_business

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.databinding.FragmentAddBusinessDetailsBinding
import com.koinvois.generator.ui.setting.SettingViewModel
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddBusinessDetailsFragment : Fragment() {

    private var binding: FragmentAddBusinessDetailsBinding? = null
    private val viewModel: SettingViewModel by hiltNavGraphViewModels(R.id.setting_navigation_graph)

    val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        val path = getPathFromURI(selectedImageUri)
                        withContext(Dispatchers.Main) {
                            binding?.imgImagePicker?.setImageURI(selectedImageUri)
                        }
                    }
                }
            }
        }

    val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                binding?.imgImagePicker?.setImageBitmap(imageBitmap)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBusinessDetailsBinding.inflate(inflater, container, false)
        setUpToolbar()
        backPressed()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getBusiness()
        }
        setObserver()
        setClickListeners()

        return binding?.root
    }

    private fun setObserver() {
        viewModel.businessUpdateModel.observe(viewLifecycleOwner) { pb ->

            Log.e("logo", pb?.businessLogo.toString())
            binding?.let {
                with(it) {
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
        }
    }

    private fun setClickListeners() {
        binding?.imgImagePicker?.setSafeOnClickListener {

            activity?.let {
                createBottomSheetDialog(it)
            }
        }

        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            activity?.let { it1 -> saveOnBack(it1) }
        }
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.title_business_details)
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            activity?.let {
                saveOnBack(it)
            }
        }
    }

    private fun saveOnBack(context: Context) {

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {

            if (binding?.editEmail?.getStringText()?.isNotEmpty() == true) {
                if (binding?.editEmail?.isEmail() == true) {
                    exit()
                } else {
                    binding?.root?.showErrorSnackbar(getString(R.string.error_invalid_email))
                }
            } else {
                exit()
            }
        }
    }

    private suspend fun exit() {
        binding?.let {
            with(it) {
                val business = PersonalBusiness(
                    1,
                    imgImagePicker.drawable.toBitmap(),
                    it.editBusinessName.getString(),
                    it.editBusinessOwnerName.getString(),
                    it.editBusinessNumber.getString(),
                    it.editAddress1.getString(),
                    it.editAddress2.getString(),
                    it.editAddress3.getString(),
                    it.editEmail.getString(),
                    it.editPhoneNumber.getInt(),
                    it.editMobileNumber.getInt(),
                    it.editWebsite.getString()
                )

                viewModel.updateBusiness(business)
            }
        }
        findNavController().navigateUp()
    }

    private suspend fun getPathFromURI(uri: Uri?): String {
        val contentResolver: ContentResolver? = activity?.contentResolver
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun createBottomSheetDialog(context: Activity) {
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
}