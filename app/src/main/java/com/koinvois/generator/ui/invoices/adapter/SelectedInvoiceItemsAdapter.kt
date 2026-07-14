package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.InvoiceItem
import com.koinvois.generator.databinding.ItemItemsInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener

class SelectedInvoiceItemsAdapter(
    private val selectedItemsList: ArrayList<InvoiceItem>,
    private val viewModel: InvoiceMainViewModel,
    private val onItemClick: (InvoiceItem) -> Unit
) :
    RecyclerView.Adapter<SelectedInvoiceItemsAdapter
    .SelectedInvoiceItemsViewHolder>
        () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedInvoiceItemsViewHolder {
        val binding = ItemItemsInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedInvoiceItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedInvoiceItemsViewHolder, position: Int) {
        holder.bind(selectedItemsList[position])

        holder.binding.root.setSafeOnClickListener {
            viewModel.currentInvoiceItem = selectedItemsList[position]
            onItemClick(selectedItemsList[position])
        }
    }

    override fun getItemCount(): Int {
        return selectedItemsList.size ?: 0
    }

    class SelectedInvoiceItemsViewHolder(val binding: ItemItemsInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(singleInvoiceItem: InvoiceItem?) {

            with(binding) {
                txtItemName.text = singleInvoiceItem?.invoiceItemName
                txtItemDescription.text = singleInvoiceItem?.invoiceItemDetails ?: ""
                txtItemQuantity.text = singleInvoiceItem?.invoiceItemQuantity.toString()
                txtItemUnitCost.text = "Rp${singleInvoiceItem?.invoiceItemUnitCost}"
                txtItemTotalCost.text = "Rp${singleInvoiceItem?.itemTotal}"
            }
        }
    }
}