package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.FragmentItemListForEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.AllItemsForEstimateAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemListForEstimateActivity : BaseActivity<FragmentItemListForEstimateBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()

    override fun inflateBinding(): FragmentItemListForEstimateBinding =
        FragmentItemListForEstimateBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setToolbar()
        setRecyclerView()
        setClickListeners()
    }

    private fun setToolbar() {
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_items)
    }

    private fun setRecyclerView() {
        lifecycleScope.launch(Dispatchers.Main) {
            val allItems = viewModel.allItems
            if (allItems?.isEmpty() == true) {
                binding.rvAllItems.hide()
                binding.txtEmptyItemList.visible()
            } else {
                binding.rvAllItems.visible()
                binding.txtEmptyItemList.hide()
                binding.rvAllItems.adapter = allItems?.let { AllItemsForEstimateAdapter(it, viewModel) { finish() } }
            }
        }
    }

    private fun setClickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            finish()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ItemListForEstimateActivity::class.java)
    }
}
