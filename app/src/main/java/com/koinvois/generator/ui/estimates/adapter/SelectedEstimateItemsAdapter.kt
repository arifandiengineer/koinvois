package com.koinvois.generator.ui.estimates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.EstimateItem
import com.koinvois.generator.databinding.ItemItemsInvoiceBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.add_estimate.AddEstimateMainFragmentDirections
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible

class SelectedEstimateItemsAdapter(
    private val selectedItemsList: ArrayList<EstimateItem>,
    private val navController: NavController,
    private val viewModel: EstimatesMainViewModel
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
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentItemDetailForEstimate(
                    DBEnum.OLD.entryType
                )
            navController.navigate(action)
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