package com.koinvois.generator.ui.item.add_item

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.databinding.ActivityItemAddBinding
import com.koinvois.generator.ui.item.ItemMainViewModel
import com.koinvois.generator.utilities.constants.Constants
import com.koinvois.generator.utilities.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddItemActivity : BaseActivity<ActivityItemAddBinding>() {

    private val viewModel: ItemMainViewModel by viewModels()
    private val itemId: Int by lazy { intent.getIntExtra(EXTRA_ITEM_ID, -1) }
    private val itemType: String get() = if (itemId != -1) Constants.EXISTING_ITEM else Constants.NEW_ITEM
    private var currentItem: Item? = null

    override fun inflateBinding(): ActivityItemAddBinding =
        ActivityItemAddBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }

        if (itemType == Constants.EXISTING_ITEM) {
            lifecycleScope.launch(Dispatchers.Main) {
                currentItem = viewModel.getItemById(itemId)
                populateFields(currentItem)
            }
        }
    }

    private fun populateFields(item: Item?) {
        item ?: return
        with(binding) {
            editItemDescription.setText(item.itemName)
            editItemUnitCost.setText(item.itemUnitCost.toString())
            editAdditionalItemDetails.setText(item.itemDetails)
            switchItemTaxable.isChecked = item.itemTaxable ?: false
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = if (itemType == Constants.EXISTING_ITEM)
            getString(R.string.label_edit_item) else getString(R.string.label_add_item)

        if (itemType == Constants.EXISTING_ITEM) {
            binding.customToolbar.imgRightAction.visible()
            binding.customToolbar.imgRightAction.setImageResource(R.drawable.icon_three_dot)
            binding.customToolbar.imgRightAction.setSafeOnClickListener {
                val popup = PopupMenu(this, it)
                popup.menuInflater.inflate(R.menu.popup_menu_client, popup.menu)
                popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.title) {
                            getString(R.string.delete_menu_item) -> {
                                BaseDialog.confirm(
                                    context = this@AddItemActivity,
                                    title = getString(R.string.delete_confirm_title),
                                    message = getString(R.string.delete_item_confirm_message),
                                    positiveText = getString(R.string.delete_confirm_positive),
                                    negativeText = getString(R.string.delete_confirm_negative),
                                    onConfirm = {
                                        lifecycleScope.launch(Dispatchers.Main) {
                                            currentItem?.let { it1 -> viewModel.deleteItem(it1) }
                                            finish()
                                        }
                                    }
                                )
                            }
                        }
                        return true
                    }
                })
                popup.show()
            }
        }

        binding.customToolbar.btnBack.setOnClickListener {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding.editItemDescription.text.toString().isNotEmpty()) {
            when (itemType) {
                Constants.NEW_ITEM -> {
                    val newItem = with(binding) {
                        Item(
                            0,
                            editItemDescription.getString(),
                            editItemUnitCost.getFloat(),
                            switchItemTaxable.isChecked,
                            editAdditionalItemDetails.getString(),
                        )
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        viewModel.addItem(newItem)
                    }
                }
                Constants.EXISTING_ITEM -> {
                    val updatedItem = currentItem?.itemId?.let { id ->
                        with(binding) {
                            Item(
                                id,
                                editItemDescription.getString(),
                                editItemUnitCost.getFloat(),
                                switchItemTaxable.isChecked,
                                editAdditionalItemDetails.getString(),
                            )
                        }
                    }
                    updatedItem?.let { it1 ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.updateItem(it1)
                        }
                    }
                }
            }
            finish()
        } else {
            finish()
        }
    }

    companion object {
        private const val EXTRA_ITEM_ID = "extra_item_id"

        fun newIntent(context: Context, itemId: Int = -1): Intent =
            Intent(context, AddItemActivity::class.java).apply {
                putExtra(EXTRA_ITEM_ID, itemId)
            }
    }
}
