package com.koinvois.generator.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
            txtStatus.text = if (isPaid) root.context.getString(R.string.status_paid)
                else root.context.getString(R.string.status_unpaid)
            txtStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(root.context, if (isPaid) R.color.color_stat_green_bg else R.color.color_stat_orange_bg)
            )
            txtStatus.setTextColor(
                ContextCompat.getColor(root.context, if (isPaid) R.color.primary_color else R.color.color_stat_orange)
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
