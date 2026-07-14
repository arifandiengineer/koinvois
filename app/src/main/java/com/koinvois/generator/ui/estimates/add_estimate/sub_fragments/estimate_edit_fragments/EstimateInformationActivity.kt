package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.ActivityEstimateInfoBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.manager.DialogManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstimateInformationActivity : BaseActivity<ActivityEstimateInfoBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()

    override fun inflateBinding(): ActivityEstimateInfoBinding =
        ActivityEstimateInfoBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        onBackPressedDispatcher.addCallback(this) { saveOnExit() }
        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.estimateNumber?.let { binding.editEstimateNumber.setText(it.toString()) }
            ?: run { binding.editEstimateNumber.setText(null) }

        viewModel.estimateDate?.let {
            binding.editEstimateDate.text = it
        }

        viewModel.estimatePoNumber?.let {
            binding.editEstimatePoNumber.setText(it)
        }
    }

    fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnExit()
        }

        binding.editEstimateDate.setSafeOnClickListener {
            val datePicker = DialogManager.makeDatePickerDialog(this) { date ->
                binding.editEstimateDate.text = date
            }
            datePicker.show()
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_estimate_number)
    }

    private fun saveOnExit() {
        if (binding.txtEstimateNumber.text?.isNotEmpty() == true) {
            viewModel.estimateNumber = binding.editEstimateNumber.getInt()
            viewModel.estimateDate = binding.editEstimateDate.text.toString()
            viewModel.estimatePoNumber = binding.editEstimatePoNumber.getStringText()
        } else {
            Toast.makeText(this, getString(R.string.error_invalid_estimate_number), Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EstimateInformationActivity::class.java)
    }
}
