package com.koinvois.generator.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.R
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.databinding.ItemRecentInvoiceBinding
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.utilities.extensions.visible

class RecentInvoiceAdapter(
    private val onInvoiceClicked: (Invoice) -> Unit,
) : BaseRecyclerAdapter<Invoice, ItemRecentInvoiceBinding>(RecentInvoiceDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRecentInvoiceBinding =
        ItemRecentInvoiceBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemRecentInvoiceBinding, item: Invoice, position: Int) {
        with(binding) {
            txtClientName.text = item.invoiceClientName ?: root.context.getString(R.string.fallback_no_client)
            txtInvoiceDetails.text = String.format("INV-%04d • %s", item.invoiceNumber ?: 0, item.invoiceDate ?: "")
            txtAmount.text = CurrencyFormatter.format(item.invoiceTotal ?: 0f)
            
            // Status badge logic
            val isPaid = item.invoiceStatus?.equals("PAID", ignoreCase = true) == true
            txtStatus.text = if (isPaid) "PAID" else "UNPAID"
            txtStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor(if (isPaid) "#E8F5E9" else "#FFF3E0")
            )
            txtStatus.setTextColor(
                android.graphics.Color.parseColor(if (isPaid) "#4CAF50" else "#FF9800")
            )

            root.setOnClickListener { onInvoiceClicked(item) }
        }
    }

    class RecentInvoiceDiffCallback : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem.invoiceId == newItem.invoiceId

        override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem.copy(invoiceSignature = null) == newItem.copy(invoiceSignature = null)
    }
}
