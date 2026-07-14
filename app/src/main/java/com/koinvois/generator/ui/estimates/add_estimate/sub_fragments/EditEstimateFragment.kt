package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.koinvois.generator.databinding.FragmentEditEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.SelectedEstimateItemsAdapter
import com.koinvois.generator.ui.estimates.adapter.SelectedPhotosForEstimateAdapter
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.AddPhotoToEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ClientDetailForEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ClientListForEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EditBusinessDetailsFromEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateDiscountActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateInformationActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateSignatureActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateTaxActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ItemDetailForEstimateActivity
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.showErrorSnackbar

class EditEstimateFragment : Fragment() {

    private var binding: FragmentEditEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditEstimateBinding.inflate(inflater, container, false)

        setClickListeners()

        return binding?.root
    }

    private fun setClickListeners() {

        binding?.txtSignature?.setSafeOnClickListener {
            startActivity(EstimateSignatureActivity.newIntent(requireContext()))
        }

        binding?.txtEstimateDate?.setSafeOnClickListener {
            startActivity(EstimateInformationActivity.newIntent(requireContext()))
        }

        binding?.txtEstimateNumber?.setSafeOnClickListener {
            startActivity(EstimateInformationActivity.newIntent(requireContext()))
        }

        binding?.txtBusinessInfo?.setSafeOnClickListener {
            startActivity(EditBusinessDetailsFromEstimateActivity.newIntent(requireContext()))
        }

        binding?.secondCard?.setSafeOnClickListener {

            viewModel.selectedClient?.let {
                startActivity(ClientDetailForEstimateActivity.newIntent(requireContext()))
            } ?: run {
                when (viewModel.allClients?.isNotEmpty()) {
                    true -> {
                        startActivity(ClientListForEstimateActivity.newIntent(requireContext()))
                    }
                    false -> {
                        startActivity(ClientDetailForEstimateActivity.newIntent(requireContext()))
                    }
                    null -> {
                        binding?.root?.showErrorSnackbar(getString(com.koinvois.generator.R.string.error_try_again))
                    }
                }
            }
        }

        binding?.txtAddItem?.setSafeOnClickListener {
            startActivity(ItemDetailForEstimateActivity.newIntent(requireContext(), DBEnum.NEW.entryType))
        }

        binding?.txtDiscountPrice?.setSafeOnClickListener {
            startActivity(EstimateDiscountActivity.newIntent(requireContext()))
        }

        binding?.txtTaxPrice?.setSafeOnClickListener {
            startActivity(EstimateTaxActivity.newIntent(requireContext()))
        }

        binding?.txtAddPhoto?.setSafeOnClickListener {
            startActivity(AddPhotoToEstimateActivity.newIntent(requireContext(), DBEnum.NEW.entryType))
        }

        binding?.btnMarkPaid?.setSafeOnClickListener {
            when (viewModel.estimateStatus) {
                EstimateStatusEnum.OPEN.status -> {
                    viewModel.estimateStatus = EstimateStatusEnum.CLOSED.status
                    binding?.btnMarkPaid?.setText("Mark Open")
                }
                EstimateStatusEnum.CLOSED.status -> {
                    viewModel.estimateStatus = EstimateStatusEnum.OPEN.status
                    binding?.btnMarkPaid?.setText("Mark Closed")
                }
                else -> {
                    viewModel.estimateStatus = EstimateStatusEnum.CLOSED.status
                    binding?.btnMarkPaid?.setText("Mark Open")
                }
            }
        }

        binding?.txtNotes?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val notes = binding?.txtNotes?.text.toString()
                viewModel.estimateNotes = notes
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    override fun onResume() {
        super.onResume()

        viewModel.selectedClient.let {
            binding?.txtClientName?.text = it?.clientName
        }

        viewModel.signatureObj?.let {
            binding?.txtSignature?.text = "Signed on ${it?.signatureDate}"

        }
        var totalItemsCost = 0f

        viewModel.selectedItemsList?.forEach {
            it.toString().let { it1 -> Log.e("obj", it1) }
            totalItemsCost += it.itemTotal ?: 0f
        }
        binding?.txtTotalAmount?.text = totalItemsCost.toString()

        viewModel.estimateNumber?.let {
            binding?.txtEstimateNumber?.text = it.toString()
        } ?: run {
            binding?.txtEstimateNumber?.text = null
        }

        viewModel.estimateDate?.let {
            binding?.txtEstimateDate?.text = it
        }

        viewModel.selectedItemsList?.let {
            binding?.rvEstimateItems?.adapter =
                SelectedEstimateItemsAdapter(it, viewModel) {
                    startActivity(ItemDetailForEstimateActivity.newIntent(requireContext(), DBEnum.OLD.entryType))
                }
        }

        viewModel.photosForEstimate?.let {
            binding?.rvPhotos?.adapter =
                SelectedPhotosForEstimateAdapter(it, viewModel) {
                    startActivity(AddPhotoToEstimateActivity.newIntent(requireContext(), DBEnum.OLD.entryType))
                }
        }

        viewModel.discountAmount?.let {
            binding?.txtDiscountPrice?.text = it.toString()
        }

        viewModel.estimateNotes?.let {
            binding?.txtNotes?.setText(it)
        }

        viewModel.estimateStatus?.let {
            when (it) {
                EstimateStatusEnum.OPEN.status -> {
                    binding?.btnMarkPaid?.setText("Mark Closed")

                }
                EstimateStatusEnum.CLOSED.status -> {
                    binding?.btnMarkPaid?.setText("Mark Open")

                }
                else -> {
                    binding?.btnMarkPaid?.setText("Mark Closed")
                }
            }
        }
    }
}
