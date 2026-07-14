package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.ActivityInvoiceDiscountBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscountActivity : BaseActivity<ActivityInvoiceDiscountBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()

    override fun inflateBinding(): ActivityInvoiceDiscountBinding =
        ActivityInvoiceDiscountBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }
    }

    private fun setClickListeners() {
        binding.editDiscountType.setSafeOnClickListener {
            createBottomSheetDialog(this)
        }

        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnBack()
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.label_discount)
    }

    private fun createBottomSheetDialog(context: android.app.Activity) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_discount)

        val btnNoDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtNoDiscount)
        val btnPercentageDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtPercentage)
        val btnFlatAmount = bottomSheetDialog.findViewById<TextView>(R.id.txtFlatAmount)

        btnNoDiscount?.setSafeOnClickListener {
            binding.editDiscountType.text = ItemDiscountTypeEnum.NO_DISCOUNT.discountTypeCapital
            binding.discountAmountGroup.hide()
            bottomSheetDialog.dismiss()
        }

        btnPercentageDiscount?.setSafeOnClickListener {
            binding.editDiscountType.text = ItemDiscountTypeEnum.PERCENTAGE.discountTypeCapital
            binding.discountAmountGroup.visible()
            bottomSheetDialog.dismiss()
        }

        btnFlatAmount?.setSafeOnClickListener {
            binding.editDiscountType.text = ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeCapital
            binding.discountAmountGroup.visible()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun saveOnBack() {
        viewModel.discountType = binding.editDiscountType.getStringText()
        viewModel.discountAmount = binding.editDiscountAmount.getFloatText()

        viewModel.recalculateInvoiceTotal()

        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, DiscountActivity::class.java)
    }
}
