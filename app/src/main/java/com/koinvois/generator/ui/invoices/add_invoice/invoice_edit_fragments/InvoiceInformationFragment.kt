package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentInvoiceInformationBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.InvoiceTermsEnums
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.manager.DialogManager
import com.google.android.material.bottomsheet.BottomSheetDialog

class InvoiceInformationFragment : Fragment() {

    var binding: FragmentInvoiceInformationBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceInformationBinding.inflate(inflater, container, false)

        activity?.let {
            setClickListeners(it)
        }
        setUpToolbar()
        backPressed()
        setObservers()


        return binding?.root
    }

    private fun setObservers() {
        viewModel.invoiceNumber?.let { binding?.editInvoiceNumber?.setText(it.toString()) }
            ?: run { binding?.editInvoiceNumber?.setText(null) }

        viewModel.invoiceDate?.let {
            binding?.editInvoiceDate?.text = it
        }

        viewModel.invoiceDueDate?.let {
            binding?.editInvoiceDueDate?.text = it
        }

        viewModel.invoicePoNumber?.let {
            binding?.editInvoicePoNumber?.setText(it)
        }
    }

    private fun setClickListeners(context: Context) {
        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            saveOnExit()
        }

        binding?.editInvoiceDate?.setSafeOnClickListener {
            val datePicker = activity?.let { it1 ->
                DialogManager.makeDatePickerDialog(it1) { date ->
                    binding?.editInvoiceDate?.text = date
                }
            }
            datePicker?.show()
        }

        binding?.editInvoiceDueDate?.setSafeOnClickListener {
            val datePicker = activity?.let { it1 ->
                DialogManager.makeDatePickerDialog(it1) { date ->
                    binding?.editInvoiceDueDate?.text = date
                }
            }
            datePicker?.show()
        }

        binding?.editInvoiceTerms?.setSafeOnClickListener {
            createBottomSheetDialog(context)
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnExit()
        }
    }

    private fun setUpToolbar() {
        binding?.let {
            with(it) {
                customToolbar.txtToolbarTitle.text = getString(R.string.title_invoice_number)
            }
        }
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
            binding?.editInvoiceTerms?.text = InvoiceTermsEnums.NONE.stringValue
            bottomSheetDialog.dismiss()
        }

        btnDueOnReceipt?.setSafeOnClickListener {
            binding?.editInvoiceTerms?.text = InvoiceTermsEnums.DUE_ON_RECEIPT.stringValue
            bottomSheetDialog.dismiss()
        }

        btnNextDay?.setSafeOnClickListener {
            binding?.editInvoiceTerms?.text = InvoiceTermsEnums.NEXT_DAY.stringValue
            bottomSheetDialog.dismiss()
        }
        btnTwoDay?.setSafeOnClickListener {
            binding?.editInvoiceTerms?.text = InvoiceTermsEnums.TWO_DAYS.stringValue
            bottomSheetDialog.dismiss()
        }

        btnOneWeek?.setSafeOnClickListener {
            binding?.editInvoiceTerms?.text = InvoiceTermsEnums.ONE_WEEK.stringValue
            bottomSheetDialog.dismiss()
        }

        btnCustom?.setSafeOnClickListener {
            binding?.editInvoiceTerms?.text = InvoiceTermsEnums.CUSTOM.stringValue
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun saveOnExit() {

        if (binding?.txtInvoiceNumber?.text?.isNotEmpty() == true) {
            viewModel.invoiceNumber = binding?.editInvoiceNumber?.getInt()
            viewModel.invoiceDate = binding?.editInvoiceDate?.text.toString()
            viewModel.invoiceDueDate = binding?.editInvoiceDueDate?.text?.toString()
            viewModel.invoicePoNumber = binding?.editInvoicePoNumber?.getStringText()

            Log.e("text", binding?.editInvoiceDate?.text.toString())

        } else {
            Toast.makeText(activity, getString(R.string.error_invalid_invoice_number), Toast.LENGTH_SHORT).show()
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}


