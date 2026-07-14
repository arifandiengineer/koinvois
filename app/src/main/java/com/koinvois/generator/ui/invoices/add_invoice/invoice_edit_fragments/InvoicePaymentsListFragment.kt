package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentInvoicePaymentsListBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.PaymentsAdapter
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import kotlinx.coroutines.launch

class InvoicePaymentsListFragment : Fragment() {

    private var binding: FragmentInvoicePaymentsListBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoicePaymentsListBinding.inflate(inflater, container, false)

        setToolbar()
        setClickListeners()
        observeViewModel()

        return binding?.root
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe payments list
                launch {
                    viewModel.invoicePaymentsState.collect { payments ->
                        binding?.rvPayments?.adapter = PaymentsAdapter(ArrayList(payments))
                    }
                }

                // Observe total paid
                launch {
                    viewModel.totalPaidAmount.collect { total ->
                        binding?.txtPaidAmount?.text = String.format("%.2f", total)
                    }
                }

                // Observe balance due
                launch {
                    viewModel.balanceDue.collect { balance ->
                        binding?.txtBalanceDueAfterPaymentAmount?.text = String.format("%.2f", balance)
                    }
                }
            }
        }
    }

    private fun setClickListeners() {
        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            findNavController().navigateUp()
        }

        binding?.txtAddPayment?.setSafeOnClickListener {
            val action = InvoicePaymentsListFragmentDirections.actionFragmentPaymentsToAddPayment()
            findNavController().navigate(action)
        }
    }

    private fun setToolbar() {
        binding?.let {
            with(it) {
                customToolbar.txtToolbarTitle.text = getString(R.string.label_payment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
