package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.InvoiceItem
import com.koinvois.generator.databinding.ItemItemsInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainFragmentDirections
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible

class SelectedInvoiceItemsAdapter(
    private val selectedItemsList: ArrayList<InvoiceItem>,
    private val navController: NavController,
    private val viewModel: InvoiceMainViewModel
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
            val action = AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentItemDetailForInvoice(DBEnum.OLD.entryType)
            navController.navigate(action)
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