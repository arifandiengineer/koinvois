package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentEstimateInformationBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.manager.DialogManager

class EstimateInformationFragment : Fragment() {

    private var binding: FragmentEstimateInformationBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstimateInformationBinding.inflate(inflater, container, false)

        setUpToolbar()
        backPressed()
        setClickListeners()
        setObservers()

        return binding?.root
    }

    private fun setObservers() {
        viewModel.estimateNumber?.let { binding?.editEstimateNumber?.setText(it.toString()) }
            ?: run { binding?.editEstimateNumber?.setText(null) }

        viewModel.estimateDate?.let {
            binding?.editEstimateDate?.text = it
        }

        viewModel.estimatePoNumber?.let {
            binding?.editEstimatePoNumber?.setText(it)
        }
    }

    fun setClickListeners() {
        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            saveOnExit()
        }

        binding?.editEstimateDate?.setSafeOnClickListener {
            val datePicker = activity?.let { it1 ->
                DialogManager.makeDatePickerDialog(it1) { date ->
                    binding?.editEstimateDate?.text = date
                }
            }
            datePicker?.show()
        }
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.title_estimate_number)
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnExit()
        }
    }

    private fun saveOnExit() {

        if (binding?.txtEstimateNumber?.text?.isNotEmpty() == true) {
            viewModel.estimateNumber = binding?.editEstimateNumber?.getInt()
            viewModel.estimateDate = binding?.editEstimateDate?.text.toString()
            viewModel.estimatePoNumber = binding?.editEstimatePoNumber?.getStringText()

        } else {
            Toast.makeText(activity, getString(R.string.error_invalid_estimate_number), Toast.LENGTH_SHORT).show()
        }
        findNavController().navigateUp()
    }

}