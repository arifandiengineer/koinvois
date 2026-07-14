package com.koinvois.generator.ui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.databinding.ItemItemsBinding
import com.koinvois.generator.ui.item.ItemMainFragmentDirections
import com.koinvois.generator.ui.item.ItemMainViewModel
import com.koinvois.generator.utilities.constants.Constants

class AllItemsAdapter(private val navController: NavController, val viewModel: ItemMainViewModel) :
    BaseRecyclerAdapter<Item, ItemItemsBinding>(ItemDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemItemsBinding =
        ItemItemsBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemItemsBinding, item: Item, position: Int) {
        binding.txtItemName.text = item.itemName
        binding.txtItemCost.text = item.itemUnitCost.toString()
        binding.txtItemDescription.text = item.itemDetails

        binding.root.setOnClickListener {
            viewModel.itemUpdateModel.postValue(item)
            val action = ItemMainFragmentDirections.actionMainItemFragmentToAddItemFragment(Constants.EXISTING_ITEM)
            navController.navigate(action)
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.itemId == newItem.itemId

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem == newItem
    }
}
