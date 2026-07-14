package com.koinvois.generator.ui.invoices.sub_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentOutstandingInvoicesBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.AllInvoiceAdapter
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible

class OutstandingInvoices : Fragment() {

    var binding: FragmentOutstandingInvoicesBinding? = null
    val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    private var allInvoiceAdapter: AllInvoiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOutstandingInvoicesBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        return binding?.root
    }

    private fun setUpRecyclerView() {
        viewModel.allInvoicesLive.observe(viewLifecycleOwner) {

            var outStandingInvoices = it.filter { invoice ->
                invoice.invoiceStatus == InvoiceStatusEnum.UN_PAID.status
            }
            if (outStandingInvoices.isNotEmpty()) {

                val adapter = allInvoiceAdapter ?: AllInvoiceAdapter(
                    viewModel,
                    findNavController(),
                    viewLifecycleOwner
                ).also { newAdapter ->
                    allInvoiceAdapter = newAdapter
                    binding?.rvOutstandingInvoices?.adapter = newAdapter
                }
                adapter.submitList(outStandingInvoices.sortedByDescending { invoice -> invoice.invoiceId })

                binding?.rvOutstandingInvoices?.visible()
                binding?.txtEmptyInvoiceListList?.hide()
            } else {
                binding?.rvOutstandingInvoices?.hide()
                binding?.txtEmptyInvoiceListList?.visible()
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}