package com.koinvois.generator.ui.client

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.common.state.bind
import com.koinvois.generator.databinding.ActivityClientMainBinding
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.ui.client.add_client.AddClientActivity
import com.koinvois.generator.ui.client.adapter.AllClientsAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientMainActivity : BaseActivity<ActivityClientMainBinding>() {

    private val viewModel: ClientMainViewModel by viewModels()

    override fun inflateBinding(): ActivityClientMainBinding =
        ActivityClientMainBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners()

        lifecycleScope.launch(Dispatchers.Main) {
            val allClients = viewModel.getAllClients()
            if (allClients.isEmpty()) {
                binding.txtName.hide()
                binding.txtTotalBilled.hide()
                binding.rvAllClients.hide()
                binding.emptyState.root.visible()
                binding.emptyState.bind(title = getString(R.string.no_clients_empty_state))
            } else {
                binding.txtName.visible()
                binding.txtTotalBilled.visible()
                binding.rvAllClients.visible()
                binding.emptyState.root.hide()
                setUpRecyclerView(allClients)
            }
        }
    }

    private fun setUpRecyclerView(allClients: ArrayList<Client>) {
        val adapter = AllClientsAdapter { client ->
            startActivity(AddClientActivity.newIntent(this, client.clientId))
        }
        binding.rvAllClients.adapter = adapter
        adapter.submitList(allClients)
    }

    private fun setUpToolbar() {
        binding.customToolbar.imgRightAction.visible()
        binding.customToolbar.imgSecondaryAction.inVisible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_clients)
    }

    private fun setClickListeners() {
        binding.btnAddClient.setOnClickListener {
            startActivity(AddClientActivity.newIntent(this))
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ClientMainActivity::class.java)
    }
}
