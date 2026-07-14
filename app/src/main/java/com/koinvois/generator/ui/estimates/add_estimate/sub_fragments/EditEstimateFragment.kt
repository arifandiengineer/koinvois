package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentEditEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.SelectedEstimateItemsAdapter
import com.koinvois.generator.ui.estimates.adapter.SelectedPhotosForEstimateAdapter
import com.koinvois.generator.ui.estimates.add_estimate.AddEstimateMainFragmentDirections
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.showErrorSnackbar

class EditEstimateFragment : Fragment() {

    private var binding: FragmentEditEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)
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
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEstimateEditToSignatureEstimate()
            findNavController().navigate(action)
        }

        binding?.txtEstimateDate?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentEstimateInformation()
            findNavController().navigate(action)
        }

        binding?.txtEstimateNumber?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentEstimateInformation()
            findNavController().navigate(action)
        }

        binding?.txtBusinessInfo?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentBusinessDetailsFromEstimate()
            findNavController().navigate(action)
        }

        binding?.secondCard?.setSafeOnClickListener {

            viewModel.selectedClient?.let {
                val action =
                    AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentClientDetailForEstimate()
                findNavController().navigate(action)
            } ?: run {
                when (viewModel.allClients?.isNotEmpty()) {
                    true -> {
                        val action =
                            AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentClientsForEstimate()
                        findNavController().navigate(action)
                    }
                    false -> {
                        val action =
                            AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentClientDetailForEstimate()
                        findNavController().navigate(action)
                    }
                    null -> {
                        binding?.root?.showErrorSnackbar(getString(R.string.error_try_again))
                    }
                }
            }
        }

        binding?.txtAddItem?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentItemDetailForEstimate(
                    DBEnum.NEW.entryType
                )
            findNavController().navigate(action)
        }

        binding?.txtDiscountPrice?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentDiscountEstimate()
            findNavController().navigate(action)
        }

        binding?.txtTaxPrice?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentTaxEstimate4()
            findNavController().navigate(action)
        }

        binding?.txtAddPhoto?.setSafeOnClickListener {
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentAddPhotoToEstimate(
                    DBEnum.NEW.entryType
                )
            findNavController().navigate(action)
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

        //   binding?.txtTotalPrice?.text = totalItemsCost - viewModel.dis

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
                SelectedEstimateItemsAdapter(it, findNavController(), viewModel)
        }

        viewModel.photosForEstimate?.let {
            binding?.rvPhotos?.adapter =
                SelectedPhotosForEstimateAdapter(it, findNavController(), viewModel)
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