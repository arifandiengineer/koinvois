package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.database.models.PersonalBusiness
import com.koinvois.generator.databinding.FragmentEditBusinessDetailsFromEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditBusinessDetailsFromEstimateFragment : Fragment() {

    private var binding: FragmentEditBusinessDetailsFromEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)

    private val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (null != selectedImageUri) {
                        withContext(Dispatchers.Main) {
                            binding?.imgImagePicker?.setImageURI(selectedImageUri)
                        }
                    }
                }
            }
        }

    private val cameraLauncher =
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
        // Inflate the layout for this fragment
        binding = FragmentEditBusinessDetailsFromEstimateBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getBusiness()
            setObserver()
            setClickListeners()
            backPressed()
            setUpToolbar()
        }

        return binding?.root
    }

    private fun setUpToolbar() {

        binding?.let {
            with(it) {
                customToolbar.imgRightAction.inVisible()
                customToolbar.btnBack.visible()
                customToolbar.imgSecondaryAction.inVisible()

                customToolbar.txtToolbarTitle.text = getString(R.string.title_business_details)
            }
        }
    }

    private fun setObserver() {
        viewModel.businessUpdateModel.let { pb ->
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

    private fun saveOnBack() {

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

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

            withContext(Dispatchers.Main) {
                findNavController().navigateUp()
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
            saveOnBack()
        }

        binding?.btnSave?.setSafeOnClickListener {
            saveOnBack()
        }
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


    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
