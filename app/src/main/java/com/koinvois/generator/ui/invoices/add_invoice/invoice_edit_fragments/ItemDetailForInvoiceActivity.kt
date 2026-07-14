package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.database.models.InvoiceItem
import com.koinvois.generator.databinding.ActivityInvoiceItemDetailBinding
import com.koinvois.generator.domain.calculation.ItemCalculator
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemDetailForInvoiceActivity : BaseActivity<ActivityInvoiceItemDetailBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()
    private val itemType: String by lazy { intent.getStringExtra(EXTRA_ITEM_TYPE) ?: DBEnum.NEW.entryType }

    private var quantity = 1
    private var unitPrice = 0f
    private var dType = ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall
    private var dAmount = 0f

    override fun inflateBinding(): ActivityInvoiceItemDetailBinding =
        ActivityInvoiceItemDetailBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners()
        setEditTestListeners()
        onBackPressedDispatcher.addCallback(this) {
            lifecycleScope.launch(Dispatchers.Main) { saveOnBack() }
        }
    }

    private fun setEditTestListeners() {

        binding.editItemUnitCost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                unitPrice = if (s.toString().isNotEmpty()) {
                    s.toString().toFloat()
                } else {
                    0f
                }

                binding.txtTotalBalanceAmount.text = CurrencyFormatter.format(unitPrice * quantity)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                quantity = if (s.toString().isNotEmpty()) {
                    s.toString().toInt()
                } else {
                    1
                }
                binding.txtTotalBalanceAmount.text = CurrencyFormatter.format(unitPrice * quantity)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            lifecycleScope.launch(Dispatchers.Main)
            {
                saveOnBack()
            }
        }

        binding.switchItemTaxable.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.groupTaxRate.visible()
            } else {
                binding.groupTaxRate.hide()
            }
        }

        binding.txtItemDiscountType.setSafeOnClickListener {
            createBottomSheetDialog(this)
        }
    }

    private fun confirmDeleteItem() {
        BaseDialog.confirm(
            context = this,
            title = getString(R.string.delete_confirm_title),
            message = getString(R.string.delete_item_confirm_message),
            positiveText = getString(R.string.delete_confirm_positive),
            negativeText = getString(R.string.delete_confirm_negative),
            onConfirm = {
                lifecycleScope.launch(Dispatchers.Main) {
                    deleteItem()
                }
            },
            onCancel = {}
        )
    }

    private suspend fun deleteItem() {
        viewModel.currentInvoiceItem?.let {
            viewModel.deleteInvoiceItem(it)
            viewModel.selectedItemsList?.remove(it)
        }

        viewModel.recalculateInvoiceSubTotal()
        viewModel.recalculateInvoiceTotal()
        viewModel.currentInvoiceItem = null
        finish()
    }

    private fun createBottomSheetDialog(context: android.app.Activity) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_discount)

        val btnNoDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtNoDiscount)
        btnNoDiscount?.hide()
        val btnPercentageDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtPercentage)
        val btnFlatAmount = bottomSheetDialog.findViewById<TextView>(R.id.txtFlatAmount)

        btnPercentageDiscount?.setSafeOnClickListener {
            binding.txtItemDiscountType.text = ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall
            binding.editDiscountAmount.hint = "0%"
            bottomSheetDialog.dismiss()
        }

        btnFlatAmount?.setSafeOnClickListener {
            binding.txtItemDiscountType.text = ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeSmall
            binding.editDiscountAmount.hint = "0"
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private suspend fun saveOnBack() {
        with(binding) {
            val discountType = txtItemDiscountType.text.toString()
            val itemUnitCost = if (editItemUnitCost.text.isNotEmpty()) {
                editItemUnitCost.text?.toString()?.toFloat()
            } else {
                0f
            }
            val itemQuantity = editQuantity.getInt()
            val discountAmount = editDiscountAmount.getFloat()
            val calcResult = ItemCalculator.calculateItemTotal(
                unitCost = itemUnitCost,
                quantity = itemQuantity,
                discountType = discountType,
                discountAmount = discountAmount
            )
            if (editItemDescription.text?.isNotEmpty() == true) {

                val invoiceItem = InvoiceItem(
                    invoiceItemId = viewModel.currentInvoiceItem?.invoiceItemId,
                    invoiceIdFK = viewModel.invoicePrimaryId?.toInt(),
                    invoiceItemName = editItemDescription.getString(),
                    invoiceItemUnitCost = itemUnitCost,
                    invoiceItemQuantity = itemQuantity ?: 1,
                    itemDiscountType = discountType,
                    itemDiscountAmount = discountAmount,
                    invoiceItemTaxable = switchItemTaxable.isChecked,
                    invoiceItemDetails = editItemDetails.getString(),
                    itemTotal = calcResult.itemTotal,
                    itemTotalDiscount = calcResult.itemTotalDiscount,
                    itemTaxRate = editTaxRate.getFloat()
                )

                when (itemType) {
                    DBEnum.NEW.entryType -> {
                        viewModel.insertInvoiceItem(invoiceItem)
                        viewModel.selectedItemsList?.add(invoiceItem)
                    }

                    DBEnum.OLD.entryType -> {
                        viewModel.selectedItemsList?.indexOf(invoiceItem)

                        viewModel.updateInvoiceItem(invoiceItem)

                        viewModel.selectedItemsList?.indexOf(viewModel.currentInvoiceItem)
                            ?.let { index ->
                                viewModel.selectedItemsList?.remove(viewModel.currentInvoiceItem)
                                viewModel.selectedItemsList?.add(index, invoiceItem)
                            }
                    }

                    else -> {}
                }
                viewModel.recalculateInvoiceSubTotal()
                viewModel.recalculateInvoiceTotal()
            } else {
                Toast.makeText(this@ItemDetailForInvoiceActivity, getString(R.string.error_enter_item_name), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.currentInvoiceItem = null
        finish()
    }

    private fun setUpToolbar() {
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.more_menu_item)
        when (itemType) {
            DBEnum.NEW.entryType -> {
                binding.customToolbar.imgRightAction.setImageResource(R.drawable.icon_search)
                binding.customToolbar.imgRightAction.contentDescription = getString(R.string.cd_search)
                binding.customToolbar.imgRightAction.visible()
                binding.customToolbar.imgRightAction.setSafeOnClickListener {
                    startActivity(ItemsListForInvoiceActivity.newIntent(this))
                }
            }

            DBEnum.OLD.entryType -> {
                binding.customToolbar.imgRightAction.setImageResource(R.drawable.btn_delete)
                binding.customToolbar.imgRightAction.contentDescription = getString(R.string.cd_delete)
                binding.customToolbar.imgRightAction.visible()
                binding.customToolbar.imgRightAction.setSafeOnClickListener {
                    confirmDeleteItem()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentInvoiceItem?.let { item ->
            with(binding) {
                editItemDescription.setText(item.invoiceItemName)

                item.invoiceItemUnitCost?.let { unitCost ->
                    editItemUnitCost.setText(unitCost.toString())
                }

                item.itemDiscountType?.let { discountType ->
                    txtItemDiscountType.text = discountType
                }

                item.itemDiscountAmount?.let { discountAmount ->
                    editDiscountAmount.setText(discountAmount.toString())
                }

                item.invoiceItemTaxable.let { unitCost ->
                    switchItemTaxable.isChecked = unitCost ?: false
                }
                item.invoiceItemUnitCost?.let { unitCost ->
                    editItemUnitCost.setText(unitCost.toString())
                }

                item.itemTaxRate?.let { taxRate ->
                    editTaxRate.setText(taxRate.toString())
                }

                item.itemTotal?.let { total ->
                    txtTotalBalanceAmount.text = CurrencyFormatter.format(total)
                }

                item.invoiceItemDetails?.let { detail ->
                    editItemDetails.setText(detail)
                }
                item.invoiceItemQuantity?.let { quantity ->
                    editQuantity.setText(quantity.toString())
                }
            }
        }
    }

    companion object {
        private const val EXTRA_ITEM_TYPE = "extra_item_type"

        fun newIntent(context: Context, itemType: String): Intent =
            Intent(context, ItemDetailForInvoiceActivity::class.java).apply {
                putExtra(EXTRA_ITEM_TYPE, itemType)
            }
    }
}
