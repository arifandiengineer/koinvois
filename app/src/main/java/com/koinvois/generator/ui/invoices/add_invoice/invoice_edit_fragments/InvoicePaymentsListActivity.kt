package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.FragmentInvoicePaymentsListBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.PaymentsAdapter
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvoicePaymentsListActivity : BaseActivity<FragmentInvoicePaymentsListBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()

    override fun inflateBinding(): FragmentInvoicePaymentsListBinding =
        FragmentInvoicePaymentsListBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setToolbar()
        setClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.invoicePaymentsState.collect { payments ->
                        binding.rvPayments.adapter = PaymentsAdapter(ArrayList(payments))
                    }
                }

                launch {
                    viewModel.totalPaidAmount.collect { total ->
                        binding.txtPaidAmount.text = String.format("%.2f", total)
                    }
                }

                launch {
                    viewModel.balanceDue.collect { balance ->
                        binding.txtBalanceDueAfterPaymentAmount.text = String.format("%.2f", balance)
                    }
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            finish()
        }

        binding.txtAddPayment.setSafeOnClickListener {
            startActivity(InvoiceAddPaymentActivity.newIntent(this))
        }
    }

    private fun setToolbar() {
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.label_payment)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, InvoicePaymentsListActivity::class.java)
    }
}
