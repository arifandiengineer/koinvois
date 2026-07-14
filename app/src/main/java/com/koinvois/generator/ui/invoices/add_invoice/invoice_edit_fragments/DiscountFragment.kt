package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentDiscountBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class DiscountFragment : Fragment() {

    private var binding: FragmentDiscountBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscountBinding.inflate(inflater, container, false)

        setUpToolbar()
        setClickListeners()
        backPressed()
        return binding?.root
    }

    private fun setClickListeners() {
        binding?.editDiscountType?.setSafeOnClickListener {
            activity?.let {
                createBottomSheetDialog(it)
            }
        }

        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            saveOnBack()
        }
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.label_discount)
    }


    private fun createBottomSheetDialog(context: Activity) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_discount)

        val btnNoDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtNoDiscount)
        val btnPercentageDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtPercentage)
        val btnFlatAmount = bottomSheetDialog.findViewById<TextView>(R.id.txtFlatAmount)

        btnNoDiscount?.setSafeOnClickListener {
            binding?.editDiscountType?.text = ItemDiscountTypeEnum.NO_DISCOUNT.discountTypeCapital
            binding?.discountAmountGroup?.hide()
            bottomSheetDialog.dismiss()
        }

        btnPercentageDiscount?.setSafeOnClickListener {
            binding?.editDiscountType?.text = ItemDiscountTypeEnum.PERCENTAGE.discountTypeCapital
            binding?.discountAmountGroup?.visible()
            bottomSheetDialog.dismiss()
        }

        btnFlatAmount?.setSafeOnClickListener {
            binding?.editDiscountType?.text = ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeCapital
            binding?.discountAmountGroup?.visible()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        viewModel.discountType = binding?.editDiscountType?.getStringText()
        viewModel.discountAmount = binding?.editDiscountAmount?.getFloatText()

        viewModel.recalculateInvoiceTotal()

        // viewModel.discountPercentage = binding?.editDiscountAmount?.getFloatText()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}