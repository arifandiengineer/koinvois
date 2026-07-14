package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentItemsListForInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.AllItemsForInvoiceAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsListForInvoice : Fragment() {

    private var binding: FragmentItemsListForInvoiceBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentItemsListForInvoiceBinding.inflate(inflater, container, false)

        setToolbar()
        setRecyclerView()
        setClickListeners()

        return binding?.root
    }

    fun setClickListeners() {
        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            findNavController().navigateUp()
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
                binding?.rvAllItems?.adapter = allItems?.let { AllItemsForInvoiceAdapter(it, findNavController(), viewModel) }
            }
        }
    }

    private fun setToolbar() {
        binding?.let {
            with(it) {
                customToolbar.txtToolbarTitle.text = getString(R.string.title_items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}