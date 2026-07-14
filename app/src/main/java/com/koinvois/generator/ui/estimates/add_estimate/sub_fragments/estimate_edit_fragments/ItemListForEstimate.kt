package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentItemListForEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.AllItemsForEstimateAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemListForEstimate : Fragment() {

    private var binding: FragmentItemListForEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentItemListForEstimateBinding.inflate(inflater, container, false)

        setToolbar()
        setRecyclerView()
        setClickListeners()

        return binding?.root
    }

    private fun setToolbar() {
        binding?.let {
            with(it) {
                customToolbar.btnBack.visible()
                customToolbar.imgSecondaryAction.inVisible()
                customToolbar.imgRightAction.inVisible()
                customToolbar.imgRightAction.visible()
                customToolbar.txtToolbarTitle.text = getString(R.string.title_items)
            }
        }
    }

    private fun setRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
        {
            val allItems = viewModel.allItems
            if (allItems?.isEmpty() == true) {
                binding?.rvAllItems?.hide()
                binding?.txtEmptyItemList?.visible()
            } else {
                binding?.rvAllItems?.visible()
                binding?.txtEmptyItemList?.hide()
                binding?.rvAllItems?.adapter =
                    allItems?.let { AllItemsForEstimateAdapter(it, findNavController(), viewModel) }
            }
        }
    }

    fun setClickListeners() {
        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            findNavController().navigateUp()
        }
    }
}