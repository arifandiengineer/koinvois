package com.koinvois.generator.ui.reports.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.databinding.ItemPaidReportBinding
import com.koinvois.generator.domain.model.Invoice

class PaidReportAdapter : BaseRecyclerAdapter<Invoice, ItemPaidReportBinding>(InvoiceDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPaidReportBinding =
        ItemPaidReportBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemPaidReportBinding, item: Invoice, position: Int) {
        binding.txtClientName.text = item.invoiceClientName ?: "No Client"
        binding.txtInitials.text = item.invoiceClientName?.take(2)?.uppercase() ?: "NA"
        binding.txtInvoiceId.text = "INV-${item.invoiceNumber ?: item.invoiceId}"
        binding.txtInvoiceDate.text = item.invoiceDate ?: "No Date"
        binding.txtPaidDate.text = item.invoiceDate ?: "No Date" // Fallback as paidDate isn't in model
        binding.txtAmount.text = "$${item.invoiceTotal ?: "0.00"}"
    }

    class InvoiceDiffCallback : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem.invoiceId == newItem.invoiceId

        override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem == newItem
    }
}