package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.FragmentEstimateTaxBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.enums.ItemTaxTypeEnum
import com.koinvois.generator.utilities.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EstimateTaxActivity : BaseActivity<FragmentEstimateTaxBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()

    override fun inflateBinding(): FragmentEstimateTaxBinding =
        FragmentEstimateTaxBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setClickListeners()
        updateView()
        setUpToolbar()
        onBackPressedDispatcher.addCallback(this) {
            lifecycleScope.launch(Dispatchers.Main) { saveOnBack() }
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.label_tax)
    }

    private fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.txtTaxType.setSafeOnClickListener {
            createBottomSheetDialog(this)
        }
    }

    private fun saveOnBack() {
        viewModel.taxType = binding.txtTaxType.getStringText()
        viewModel.taxLabel = binding.editTaxLabel.getString()
        viewModel.taxRate = binding.editRate.getFloat()
        viewModel.taxInclusive = binding.switchInclusive.isChecked

        finish()
    }

    private fun createBottomSheetDialog(context: Context) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_tax)

        val btnNoTax = bottomSheetDialog.findViewById<TextView>(R.id.txtNoTax)
        val btnOnTheTotal = bottomSheetDialog.findViewById<TextView>(R.id.txtOnTheTotal)
        val btnDeducted = bottomSheetDialog.findViewById<TextView>(R.id.txtDeducted)
        val btnPerItem = bottomSheetDialog.findViewById<TextView>(R.id.txtPerItem)

        btnNoTax?.setSafeOnClickListener {
            binding.txtTaxType.text = ItemTaxTypeEnum.NONE.taxTypeCapital
            noneGroup()
            bottomSheetDialog.dismiss()
        }

        btnOnTheTotal?.setSafeOnClickListener {
            binding.txtTaxType.text = ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital
            onTheTotalGroup()
            bottomSheetDialog.dismiss()
        }

        btnDeducted?.setSafeOnClickListener {
            binding.txtTaxType.text = ItemTaxTypeEnum.DEDUCTED.taxTypeCapital
            deductedGroup()
            bottomSheetDialog.dismiss()
        }

        btnPerItem?.setSafeOnClickListener {
            binding.txtTaxType.text = ItemTaxTypeEnum.PER_ITEM.taxTypeCapital
            perItemGroup()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun updateView() {
        viewModel.taxType?.let {
            binding.txtTaxType.text = it
        }

        viewModel.taxLabel?.let {
            binding.editTaxLabel.setText(it)
        }

        viewModel.taxRate?.let {
            binding.editRate.setText(it.toString())
        }

        viewModel.taxInclusive?.let {
            binding.switchInclusive.isChecked = it
        }

        when (binding.txtTaxType.text) {
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

    private fun noneGroup() {
        binding.viewLabel.hide()
        binding.viewRate.hide()
        binding.secondCard.hide()
        binding.viewTax.visible()
    }

    private fun onTheTotalGroup() {
        binding.viewLabel.visible()
        binding.viewRate.visible()
        binding.secondCard.visible()
        binding.viewTax.visible()
    }

    private fun deductedGroup() {
        binding.viewLabel.visible()
        binding.viewRate.visible()
        binding.secondCard.hide()
        binding.viewTax.visible()
    }

    private fun perItemGroup() {
        binding.viewLabel.visible()
        binding.viewRate.hide()
        binding.secondCard.visible()
        binding.viewTax.visible()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EstimateTaxActivity::class.java)
    }
}
