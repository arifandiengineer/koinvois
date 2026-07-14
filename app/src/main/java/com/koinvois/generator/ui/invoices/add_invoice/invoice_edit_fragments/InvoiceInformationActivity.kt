package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.ActivityInvoiceInfoBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.InvoiceTermsEnums
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.manager.DialogManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceInformationActivity : BaseActivity<ActivityInvoiceInfoBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()

    override fun inflateBinding(): ActivityInvoiceInfoBinding =
        ActivityInvoiceInfoBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setClickListeners()
        setUpToolbar()
        onBackPressedDispatcher.addCallback(this) { saveOnExit() }
        setObservers()
    }

    private fun setObservers() {
        viewModel.invoiceNumber?.let { binding.editInvoiceNumber.setText(it.toString()) }
            ?: run { binding.editInvoiceNumber.setText(null) }

        viewModel.invoiceDate?.let {
            binding.editInvoiceDate.text = it
        }

        viewModel.invoiceDueDate?.let {
            binding.editInvoiceDueDate.text = it
        }

        viewModel.invoicePoNumber?.let {
            binding.editInvoicePoNumber.setText(it)
        }
    }

    private fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnExit()
        }

        binding.editInvoiceDate.setSafeOnClickListener {
            val datePicker = DialogManager.makeDatePickerDialog(this) { date ->
                binding.editInvoiceDate.text = date
            }
            datePicker.show()
        }

        binding.editInvoiceDueDate.setSafeOnClickListener {
            val datePicker = DialogManager.makeDatePickerDialog(this) { date ->
                binding.editInvoiceDueDate.text = date
            }
            datePicker.show()
        }

        binding.editInvoiceTerms.setSafeOnClickListener {
            createBottomSheetDialog(this)
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_invoice_number)
    }

    private fun createBottomSheetDialog(context: Context) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_terms)

        val btnNone = bottomSheetDialog.findViewById<TextView>(R.id.txtNoTerms)
        val btnDueOnReceipt = bottomSheetDialog.findViewById<TextView>(R.id.txtDueReceipt)
        val btnNextDay = bottomSheetDialog.findViewById<TextView>(R.id.txtNextDay)
        val btnTwoDay = bottomSheetDialog.findViewById<TextView>(R.id.txtTwoDays)
        val btnOneWeek = bottomSheetDialog.findViewById<TextView>(R.id.txtOneWeek)
        val btnCustom = bottomSheetDialog.findViewById<TextView>(R.id.txtCustom)

        btnNone?.setSafeOnClickListener {
            binding.editInvoiceTerms.text = InvoiceTermsEnums.NONE.stringValue
            bottomSheetDialog.dismiss()
        }

        btnDueOnReceipt?.setSafeOnClickListener {
            binding.editInvoiceTerms.text = InvoiceTermsEnums.DUE_ON_RECEIPT.stringValue
            bottomSheetDialog.dismiss()
        }

        btnNextDay?.setSafeOnClickListener {
            binding.editInvoiceTerms.text = InvoiceTermsEnums.NEXT_DAY.stringValue
            bottomSheetDialog.dismiss()
        }
        btnTwoDay?.setSafeOnClickListener {
            binding.editInvoiceTerms.text = InvoiceTermsEnums.TWO_DAYS.stringValue
            bottomSheetDialog.dismiss()
        }

        btnOneWeek?.setSafeOnClickListener {
            binding.editInvoiceTerms.text = InvoiceTermsEnums.ONE_WEEK.stringValue
            bottomSheetDialog.dismiss()
        }

        btnCustom?.setSafeOnClickListener {
            binding.editInvoiceTerms.text = InvoiceTermsEnums.CUSTOM.stringValue
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun saveOnExit() {
        if (binding.txtInvoiceNumber.text?.isNotEmpty() == true) {
            viewModel.invoiceNumber = binding.editInvoiceNumber.getInt()
            viewModel.invoiceDate = binding.editInvoiceDate.text.toString()
            viewModel.invoiceDueDate = binding.editInvoiceDueDate.text?.toString()
            viewModel.invoicePoNumber = binding.editInvoicePoNumber.getStringText()

            Log.e("text", binding.editInvoiceDate.text.toString())

        } else {
            Toast.makeText(this, getString(R.string.error_invalid_invoice_number), Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, InvoiceInformationActivity::class.java)
    }
}
