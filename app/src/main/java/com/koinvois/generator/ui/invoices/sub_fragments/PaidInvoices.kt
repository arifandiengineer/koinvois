package com.koinvois.generator.ui.invoices.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentPaidInvoicesBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.AllInvoiceAdapter
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible

class PaidInvoices : Fragment() {

    var binding: FragmentPaidInvoicesBinding? = null
    val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    private var allInvoiceAdapter: AllInvoiceAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaidInvoicesBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding?.root
    }

    private fun setUpRecyclerView() {
        viewModel.allInvoicesLive.observe(viewLifecycleOwner) {

            val paidInvoices = it.filter { invoice ->
                invoice.invoiceStatus == InvoiceStatusEnum.PAID.status
            }
            if (paidInvoices.isNotEmpty()) {

                val adapter = allInvoiceAdapter ?: AllInvoiceAdapter(
                    viewModel,
                    findNavController(),
                    viewLifecycleOwner
                ).also { newAdapter ->
                    allInvoiceAdapter = newAdapter
                    binding?.rvPaidInvoices?.adapter = newAdapter
                }
                adapter.submitList(paidInvoices.sortedByDescending { invoice -> invoice.invoiceId })

                binding?.rvPaidInvoices?.visible()
                binding?.txtEmptyInvoiceList?.hide()
            } else {
                binding?.rvPaidInvoices?.hide()
                binding?.txtEmptyInvoiceList?.visible()
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}