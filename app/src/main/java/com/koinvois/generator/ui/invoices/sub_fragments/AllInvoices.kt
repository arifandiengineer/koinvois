package com.koinvois.generator.ui.invoices.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.state.bind
import com.koinvois.generator.databinding.FragmentInvoiceAllBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainActivity
import com.koinvois.generator.ui.invoices.adapter.AllInvoiceAdapter
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible

class AllInvoices : Fragment() {

    private var binding: FragmentInvoiceAllBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    private var allInvoiceAdapter: AllInvoiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInvoiceAllBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        return binding?.root
    }

    private fun setUpRecyclerView() {
        viewModel.allInvoicesLive.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                val adapter = allInvoiceAdapter ?: AllInvoiceAdapter(
                    viewModel,
                    viewLifecycleOwner
                ) {
                    startActivity(AddInvoiceMainActivity.newIntent(requireContext(), DBEnum.OLD.entryType))
                }.also { newAdapter ->
                    allInvoiceAdapter = newAdapter
                    binding?.rvAllInvoices?.adapter = newAdapter
                }
                adapter.submitList(it.sortedByDescending { invoice -> invoice.invoiceId })

                binding?.rvAllInvoices?.visible()
                binding?.emptyState?.root?.hide()
            } else {
                binding?.rvAllInvoices?.hide()
                binding?.emptyState?.root?.visible()
                binding?.emptyState?.bind(title = getString(R.string.no_invoices_empty_state))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}