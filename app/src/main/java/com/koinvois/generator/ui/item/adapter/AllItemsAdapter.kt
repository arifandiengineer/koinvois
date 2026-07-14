package com.koinvois.generator.ui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.databinding.ItemItemsBinding

class AllItemsAdapter(private val onItemClick: (Item) -> Unit) :
    BaseRecyclerAdapter<Item, ItemItemsBinding>(ItemDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemItemsBinding =
        ItemItemsBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemItemsBinding, item: Item, position: Int) {
        binding.txtItemName.text = item.itemName
        binding.txtItemCost.text = item.itemUnitCost.toString()
        binding.txtItemDescription.text = item.itemDetails

        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.itemId == newItem.itemId

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem == newItem
    }
}
