package com.koinvois.generator.ui.item

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.common.state.bind
import com.koinvois.generator.databinding.ItemMainFragmentBinding
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.ui.item.add_item.AddItemActivity
import com.koinvois.generator.ui.item.adapter.AllItemsAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemMainActivity : BaseActivity<ItemMainFragmentBinding>() {

    private val viewModel: ItemMainViewModel by viewModels()

    override fun inflateBinding(): ItemMainFragmentBinding =
        ItemMainFragmentBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners()

        lifecycleScope.launch(Dispatchers.Main) {
            val allItems = viewModel.getAllItems()
            if (allItems.isEmpty()) {
                binding.rvAllItems.hide()
                binding.emptyState.root.visible()
                binding.emptyState.bind(title = getString(R.string.no_items_empty_state))
            } else {
                binding.rvAllItems.visible()
                setUpRecyclerView(allItems)
                binding.emptyState.root.hide()
            }
        }
    }

    private fun setClickListeners() {
        binding.btnAddItem.setSafeOnClickListener {
            startActivity(AddItemActivity.newIntent(this))
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.imgRightAction.visible()
        binding.customToolbar.btnBack.inVisible()
        binding.customToolbar.imgSecondaryAction.inVisible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_items)
    }

    private fun setUpRecyclerView(allItems: ArrayList<Item>) {
        val adapter = AllItemsAdapter { item ->
            startActivity(AddItemActivity.newIntent(this, item.itemId))
        }
        binding.rvAllItems.adapter = adapter
        adapter.submitList(allItems)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ItemMainActivity::class.java)
    }
}
