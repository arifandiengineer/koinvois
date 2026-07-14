package com.koinvois.generator.ui.estimates.sub_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentEstimateClosedBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.add_estimate.AddEstimateMainActivity
import com.koinvois.generator.ui.estimates.adapter.AllEstimateAdapter
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible

class ClosedEstimatesFragment : Fragment() {

    private var binding: FragmentEstimateClosedBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)
    private var allEstimateAdapter: AllEstimateAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEstimateClosedBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        return binding?.root
    }

    private fun setUpRecyclerView() {
        viewModel.allEstimatesLive.observe(viewLifecycleOwner) {

            var closedEstimates = it.filter { estimates ->
                estimates.estimateStatus == EstimateStatusEnum.CLOSED.status
            }

            if (closedEstimates.isNotEmpty()) {
                val adapter = allEstimateAdapter ?: AllEstimateAdapter(
                    viewModel,
                    viewLifecycleOwner
                ) { estimate ->
                    startActivity(AddEstimateMainActivity.newIntent(requireContext(), DBEnum.OLD.entryType, estimate.estimateId))
                }.also { newAdapter ->
                    allEstimateAdapter = newAdapter
                    binding?.rvAllEstimates?.adapter = newAdapter
                }
                adapter.submitList(closedEstimates.sortedByDescending { estimate -> estimate.estimateId })

                binding?.rvAllEstimates?.visible()
                binding?.txtEmptyEstimatesList?.hide()
            } else {
                binding?.rvAllEstimates?.hide()
                binding?.txtEmptyEstimatesList?.visible()
            }
        }
    }

}