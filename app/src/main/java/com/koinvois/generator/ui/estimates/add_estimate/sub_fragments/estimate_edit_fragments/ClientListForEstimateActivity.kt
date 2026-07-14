package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.ActivityEstimateClientListBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.AllClientsForEstimateAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientListForEstimateActivity : BaseActivity<ActivityEstimateClientListBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()

    override fun inflateBinding(): ActivityEstimateClientListBinding =
        ActivityEstimateClientListBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()

        lifecycleScope.launch(Dispatchers.Main) {
            val allClients = viewModel.allClients

            if (allClients?.isEmpty() == true) {
                binding.txtName.hide()
                binding.rvAllClients.hide()
            } else {
                binding.txtName.visible()
                binding.rvAllClients.visible()
                allClients?.let { setUpRecyclerView(it) }
            }
        }
    }

    private fun setUpRecyclerView(allClients: ArrayList<Client>) {
        binding.rvAllClients.adapter =
            AllClientsForEstimateAdapter(allClients, viewModel) { finish() }
    }

    private fun setUpToolbar() {
        with(binding) {
            customToolbar.btnBack.visible()
            customToolbar.imgSecondaryAction.inVisible()
            customToolbar.imgRightAction.inVisible()
            customToolbar.txtToolbarTitle.text = getString(R.string.title_clients)
            customToolbar.btnBack.setSafeOnClickListener {
                finish()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ClientListForEstimateActivity::class.java)
    }
}
