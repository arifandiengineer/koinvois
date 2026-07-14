package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentTaxBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.ItemTaxTypeEnum
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaxFragment : Fragment() {

    private var binding: FragmentTaxBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaxBinding.inflate(inflater, container, false)

        activity?.let {
            setClickListeners(it)
            updateView(it)
        }
        setUpToolbar()
        backPressed()

        return binding?.root
    }

    private fun setClickListeners(context: Context) {

        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            saveOnBack()
        }

        binding?.txtTaxType?.setSafeOnClickListener {
            createBottomSheetDialog(context)
        }
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.label_tax)
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
            {
                saveOnBack()
            }
        }
    }

    private fun saveOnBack() {
        viewModel.taxType = binding?.txtTaxType?.getStringText()
        viewModel.taxLabel = binding?.editTaxLabel?.getString()
        viewModel.taxRate = binding?.editRate?.getFloat()
        viewModel.taxInclusive = binding?.switchInclusive?.isChecked

        viewModel.recalculateInvoiceTotal()

        findNavController().navigateUp()
    }

    private fun createBottomSheetDialog(context: Context) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_tax)

        val btnNoTax = bottomSheetDialog.findViewById<TextView>(R.id.txtNoTax)
        val btnOnTheTotal = bottomSheetDialog.findViewById<TextView>(R.id.txtOnTheTotal)
        val btnDeducted = bottomSheetDialog.findViewById<TextView>(R.id.txtDeducted)
        val btnPerItem = bottomSheetDialog.findViewById<TextView>(R.id.txtPerItem)

        btnNoTax?.setSafeOnClickListener {
            binding?.txtTaxType?.text = ItemTaxTypeEnum.NONE.taxTypeCapital
            noneGroup()
            // binding?.discountAmountGroup?.hide()
            bottomSheetDialog.dismiss()
        }

        btnOnTheTotal?.setSafeOnClickListener {
            binding?.txtTaxType?.text = ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital
            onTheTotalGroup()
            //   binding?.discountAmountGroup?.visible()
            bottomSheetDialog.dismiss()
        }

        btnDeducted?.setSafeOnClickListener {
            binding?.txtTaxType?.text = ItemTaxTypeEnum.DEDUCTED.taxTypeCapital
            deductedGroup()
            //  binding?.discountAmountGroup?.visible()
            bottomSheetDialog.dismiss()
        }

        btnPerItem?.setSafeOnClickListener {
            binding?.txtTaxType?.text = ItemTaxTypeEnum.PER_ITEM.taxTypeCapital
            perItemGroup()
            //   binding?.discountAmountGroup?.visible()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun updateView(context: Context) {

        viewModel.taxType?.let {
            binding?.txtTaxType?.text = it
        }

        viewModel.taxLabel?.let {
            binding?.editTaxLabel?.setText(it)
        }

        viewModel.taxRate?.let {
            binding?.editRate?.setText(it.toString())
        }

        viewModel.taxInclusive?.let {
            binding?.switchInclusive?.isChecked = it
        }

        when (binding?.txtTaxType?.text) {
            ItemTaxTypeEnum.NONE.taxTypeCapital -> {
                noneGroup()
            }
            ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital -> {
                onTheTotalGroup()
            }
            ItemTaxTypeEnum.DEDUCTED.taxTypeCapital -> {
                deductedGroup()
            }
            ItemTaxTypeEnum.PER_ITEM.taxTypeCapital -> {
                perItemGroup()
            }
        }
    }

    fun noneGroup() {
        binding?.viewLabel?.hide()
        binding?.viewRate?.hide()
        binding?.secondCard?.hide()
        binding?.viewTax?.visible()
    }

    fun onTheTotalGroup() {
        binding?.viewLabel?.visible()
        binding?.viewRate?.visible()
        binding?.secondCard?.visible()
        binding?.viewTax?.visible()
    }

    fun deductedGroup() {
        binding?.viewLabel?.visible()
        binding?.viewRate?.visible()
        binding?.secondCard?.hide()
        binding?.viewTax?.visible()
    }

    fun perItemGroup() {
        binding?.viewLabel?.visible()
        binding?.viewRate?.hide()
        binding?.secondCard?.visible()
        binding?.viewTax?.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}