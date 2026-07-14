package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.Item
import com.koinvois.generator.database.models.InvoiceItem
import com.koinvois.generator.databinding.ItemItemsBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel

class AllItemsForInvoiceAdapter(
    private val itemList: ArrayList<Item>,
    private val navController: NavController,
    val viewModel: InvoiceMainViewModel
) :
    RecyclerView.Adapter<AllItemsForInvoiceAdapter.AllItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemsViewHolder {
        val binding = ItemItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemsViewHolder, position: Int) {
        holder.bind(itemList[position])

        holder.itemView.setOnClickListener {
            val item = itemList[position]
            viewModel.currentInvoiceItem =
                InvoiceItem(
                    null,
                    null,
                    item.itemName,
                    item.itemUnitCost,
                    1,
                    null,
                    null,
                    item.itemTaxable,
                    item.itemDetails,
                    null,
                    null,
                    null
                )

            navController.navigateUp()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class AllItemsViewHolder(private val binding: ItemItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.txtItemName.text = item.itemName
            binding.txtItemCost.text = item.itemUnitCost.toString()
            binding.txtItemDescription.text = item.itemDetails
        }
    }
}