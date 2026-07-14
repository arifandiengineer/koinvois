package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.koinvois.generator.R
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.database.models.EstimateItem
import com.koinvois.generator.databinding.FragmentItemDetailForEstimateBinding
import com.koinvois.generator.domain.calculation.ItemCalculator
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDetailForEstimateFragment : Fragment() {

    private var binding: FragmentItemDetailForEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)
    private val argument: ItemDetailForEstimateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentItemDetailForEstimateBinding.inflate(inflater, container, false)

        activity?.let {
            setUpToolbar()
            setClickListeners(it)
            setEditTestListeners(it)
            backPressed()
        }

        return binding?.root
    }

    private fun setUpToolbar() {
        binding?.let {
            with(it) {
                customToolbar.btnBack.visible()
                customToolbar.imgSecondaryAction.inVisible()
                customToolbar.imgRightAction.inVisible()
                when (argument.itemType) {
                    DBEnum.NEW.entryType -> {
                        customToolbar.imgRightAction.visible()
                        customToolbar.imgSecondaryAction.inVisible()
                    }

                    DBEnum.OLD.entryType -> {
                        customToolbar.imgRightAction.inVisible()
                        customToolbar.imgSecondaryAction.visible()
                    }
                }
                customToolbar.txtToolbarTitle.text = getString(R.string.more_menu_item)
            }
        }
    }

    private var quantity = 1
    private var unitPrice = 0f
    private var dType = ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall
    private var dAmount = 0f

    private fun setEditTestListeners(it: Context) {

        binding?.editItemUnitCost?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                unitPrice = if (s.toString().isNotEmpty()) {
                    s.toString().toFloat()
                } else {
                    0f
                }

                binding?.txtTotalBalanceAmount?.text = (unitPrice * quantity).toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding?.editQuantity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                quantity = if (s.toString().isNotEmpty()) {
                    s.toString().toInt()
                } else {
                    1
                }
                binding?.txtTotalBalanceAmount?.text = (unitPrice * quantity).toString()

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

//        binding?.editDiscountAmount?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                when (dType) {
//                    ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall -> {
//                        if (binding?.editDiscountAmount?.getFloat() == null) {
//                            unitPrice.times(quantity ?: 1)
//                        } else {
//                            val totalCost = unitPrice.times(quantity ?: 1)
//                            val totalDiscountAmount =
//                                (dAmount.div(100) ?: 0f).times(totalCost ?: 1f)
//                            Log.e(
//                                "pTotal",
//                                totalCost.minus(totalDiscountAmount).toString()
//                            )
//                            Log.e("pDiscount", (totalDiscountAmount).toString())
//                            //  totalDiscountAmountForItem = totalDiscountAmount
//                            binding?.txtTotalBalanceAmount?.text =
//                                totalCost.minus(totalDiscountAmount).toString()
//                        }
//                    }
//                    ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeSmall -> {
//                        if (binding?.editDiscountAmount?.getFloat() == null) {
//                            unitPrice.times(quantity ?: 1)
//                        } else {
//                            binding?.editDiscountAmount?.getFloat()
//                                ?.let { discount ->
//                                    quantity.let { quantity ->
//                                        unitPrice.times(quantity)
//                                    }.minus(discount)
//                                }
//                            val totalCost = unitPrice.times(quantity ?: 1)
//                            Log.e(
//                                "fTotal",
//                                totalCost.minus(dAmount ?: 0f).toString()
//                            )
//                            binding?.txtTotalBalanceAmount?.text =
//                                totalCost.minus(dAmount).toString()
//
//                        }
//                    }
//                    else -> {
//                        0f
//                    }
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//        })
    }

    private fun setClickListeners(context: Context) {
        binding?.customToolbar?.imgRightAction?.setSafeOnClickListener {
            val action = ItemDetailForEstimateFragmentDirections.actionFragmentItemDetailForEstimateToFragmentItemListForEstimate()
            findNavController().navigate(action)
        }

        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
            {
                saveOnBack()
            }
        }

        binding?.switchItemTaxable?.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                binding?.groupTaxRate?.visible()
            } else {
                binding?.groupTaxRate?.hide()
            }
        }

        binding?.txtItemDiscountType?.setSafeOnClickListener {
            activity?.let {
                createBottomSheetDialog(it)
            }
        }

        binding?.customToolbar?.imgSecondaryAction?.setSafeOnClickListener {
            context?.let { ctx ->
                BaseDialog.confirm(
                    context = ctx,
                    title = getString(R.string.delete_confirm_title),
                    message = getString(R.string.delete_item_confirm_message),
                    positiveText = getString(R.string.delete_confirm_positive),
                    negativeText = getString(R.string.delete_confirm_negative),
                    onConfirm = {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            deleteItem()
                        }
                    },
                    onCancel = {

                    }
                )
            }
        }
    }

    suspend fun deleteItem() {

        viewModel.currentEstimateItem?.let {
            viewModel.deleteEstimateItem(it)
            viewModel.selectedItemsList?.remove(it)
        }

        viewModel.currentEstimateItem = null
        findNavController().navigateUp()
    }

    private fun createBottomSheetDialog(context: Activity) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_discount)

        val btnNoDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtNoDiscount)
        btnNoDiscount?.hide()
        val btnPercentageDiscount = bottomSheetDialog.findViewById<TextView>(R.id.txtPercentage)
        val btnFlatAmount = bottomSheetDialog.findViewById<TextView>(R.id.txtFlatAmount)

        btnPercentageDiscount?.setSafeOnClickListener {
            binding?.txtItemDiscountType?.text = ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall
            binding?.editDiscountAmount?.hint = "0%"
            bottomSheetDialog.dismiss()
        }

        btnFlatAmount?.setSafeOnClickListener {
            binding?.txtItemDiscountType?.text = ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeSmall
            binding?.editDiscountAmount?.hint = "0"
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private suspend fun saveOnBack() {
        binding?.let {
            with(it) {
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

                    val estimateItem = EstimateItem(
                        estimateItemId = viewModel.currentEstimateItem?.estimateItemId,
                        estimateIdFK = viewModel.estimatePrimaryId?.toInt(),
                        estimateItemName = editItemDescription.getString(),
                        estimateItemUnitCost = itemUnitCost,
                        estimateItemQuantity = itemQuantity ?: 1,
                        itemDiscountType = discountType,
                        itemDiscountAmount = discountAmount,
                        estimateItemTaxable = switchItemTaxable.isChecked,
                        estimateItemDetails = editItemDetails.getString(),
                        itemTotal = calcResult.itemTotal,
                        itemTotalDiscount = calcResult.itemTotalDiscount,
                        itemTaxRate = binding?.editTaxRate?.getFloat()
                    )


                    when (argument.itemType) {
                        DBEnum.NEW.entryType -> {
                            viewModel.insertEstimateItem(estimateItem)
                            viewModel.selectedItemsList?.add(estimateItem)
                        }

                        DBEnum.OLD.entryType -> {
                            viewModel.selectedItemsList?.indexOf(estimateItem)
                            viewModel.updateEstimateItem(estimateItem)

                            viewModel.selectedItemsList?.indexOf(viewModel.currentEstimateItem)
                                ?.let { index ->
                                    viewModel.selectedItemsList?.remove(viewModel.currentEstimateItem)
                                    viewModel.selectedItemsList?.add(index, estimateItem)
                                }
                        }

                        else -> {}
                    }

                } else {
                    Toast.makeText(activity, getString(R.string.error_enter_item_name), Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.currentEstimateItem = null
        findNavController().navigateUp()
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
            {
                saveOnBack()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.currentEstimateItem?.let { item ->
            binding?.let {
                with(it) {
                    editItemDescription.setText(item.estimateItemName)

                    item.estimateItemUnitCost?.let { unitCost ->
                        editItemUnitCost.setText(unitCost.toString())
                    }

                    item.itemDiscountType?.let { discountType ->
                        txtItemDiscountType.text = discountType
                    }

                    item.itemDiscountAmount?.let { discountAmount ->
                        editDiscountAmount.setText(discountAmount.toString())
                    }

                    item.estimateItemTaxable.let { unitCost ->
                        switchItemTaxable.isChecked = unitCost ?: false
                    }
                    item.estimateItemUnitCost?.let { unitCost ->
                        editItemUnitCost.setText(unitCost.toString())
                    }

                    item.itemTaxRate?.let { taxRate ->
                        editTaxRate.setText(taxRate.toString())
                    }

                    item.itemTotal?.let { total ->
                        txtTotalBalanceAmount.text = total.toString()
                    }

                    item.estimateItemDetails?.let { detail ->
                        editItemDetails.setText(detail)
                    }
                }
            }
        }
    }
}