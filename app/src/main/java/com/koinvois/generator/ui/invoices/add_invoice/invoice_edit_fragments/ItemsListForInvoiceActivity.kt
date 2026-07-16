package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.ActivityInvoiceItemsListBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.AllItemsForInvoiceAdapter
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ItemDetailForInvoiceActivity
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemsListForInvoiceActivity : BaseActivity<ActivityInvoiceItemsListBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()

    override fun inflateBinding(): ActivityInvoiceItemsListBinding =
        ActivityInvoiceItemsListBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setToolbar()
        setClickListeners()
        
        // If empty, redirect to add new and finish this list activity
        if (viewModel.allItems.isNullOrEmpty()) {
            startActivity(ItemDetailForInvoiceActivity.newIntent(this, DBEnum.NEW.entryType))
            finish()
            return
        }
        
        setRecyclerView()
    }

    private fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            finish()
        }
    }

    private fun setRecyclerView() {
        lifecycleScope.launch(Dispatchers.Main)
        {
            val allItems = viewModel.allItems
            if (allItems?.isEmpty() == true) {
                binding.rvAllItems.hide()
                binding.txtEmptyItemList.visible()
            } else {
                binding.rvAllItems.visible()
                binding.txtEmptyItemList.hide()
                binding.rvAllItems.adapter = allItems?.let { AllItemsForInvoiceAdapter(it, viewModel) { finish() } }
            }
        }
    }

    private fun setToolbar() {
        binding.customToolbar.apply {
            txtToolbarTitle.text = getString(R.string.title_items)
            btnBack.setSafeOnClickListener { finish() }
            
            // Add Item button in toolbar
            imgRightAction.visible()
            imgRightAction.setImageResource(R.drawable.icon_add)
            imgRightAction.setSafeOnClickListener {
                startActivity(ItemDetailForInvoiceActivity.newIntent(this@ItemsListForInvoiceActivity, DBEnum.NEW.entryType))
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ItemsListForInvoiceActivity::class.java)
    }
}
