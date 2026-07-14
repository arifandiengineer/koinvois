package com.koinvois.generator.ui.estimates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.EstimateItem
import com.koinvois.generator.databinding.ItemItemsInvoiceBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible

class SelectedEstimateItemsAdapter(
    private val selectedItemsList: ArrayList<EstimateItem>,
    private val viewModel: EstimatesMainViewModel,
    private val onItemClick: (EstimateItem) -> Unit
) :
    RecyclerView.Adapter<SelectedEstimateItemsAdapter
    .SelectedInvoiceItemsViewHolder>
        () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedInvoiceItemsViewHolder {
        val binding =
            ItemItemsInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedInvoiceItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedInvoiceItemsViewHolder, position: Int) {
        holder.bind(selectedItemsList[position])

        holder.binding.root.setSafeOnClickListener {
            viewModel.currentEstimateItem = selectedItemsList[position]
            onItemClick(selectedItemsList[position])
        }
    }

    override fun getItemCount(): Int {
        return selectedItemsList.size ?: 0
    }

    class SelectedInvoiceItemsViewHolder(val binding: ItemItemsInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(singleInvoiceItem: EstimateItem?) {

            with(binding) {
                txtItemName.text = singleInvoiceItem?.estimateItemName
                txtItemDescription.text = singleInvoiceItem?.estimateItemDetails ?: ""
                txtItemQuantity.text = singleInvoiceItem?.estimateItemQuantity.toString()
                txtItemUnitCost.text = "Rp${singleInvoiceItem?.estimateItemUnitCost}"
                txtItemTotalCost.text = "Rp${singleInvoiceItem?.itemTotal}"
            }
        }
    }
}