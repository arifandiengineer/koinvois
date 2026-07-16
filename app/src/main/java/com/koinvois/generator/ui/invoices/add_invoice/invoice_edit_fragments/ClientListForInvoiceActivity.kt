package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.ActivityInvoiceClientListBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.AllClientsForInvoiceAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientListForInvoiceActivity : BaseActivity<ActivityInvoiceClientListBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()

    override fun inflateBinding(): ActivityInvoiceClientListBinding =
        ActivityInvoiceClientListBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
    }

    override fun onResume() {
        super.onResume()
        refreshClientList()
    }

    private fun refreshClientList() {
        lifecycleScope.launch(Dispatchers.Main) {
            val clients = viewModel.getAllClients()
            if (clients.isEmpty()) {
                binding.txtName.hide()
                binding.rvAllClients.hide()
            } else {
                binding.txtName.visible()
                binding.rvAllClients.visible()
                setUpRecyclerView(clients)
            }
        }
    }

    private fun setUpRecyclerView(allClients: ArrayList<Client>) {
        binding.rvAllClients.adapter =
            AllClientsForInvoiceAdapter(allClients, viewModel) { finish() }
    }

    private fun setUpToolbar() {
        binding.customToolbar.apply {
            txtToolbarTitle.text = getString(R.string.title_clients)
            btnBack.setSafeOnClickListener {
                finish()
            }
            // Add Client button in toolbar
            imgRightAction.visible()
            imgRightAction.setImageResource(R.drawable.icon_add)
            imgRightAction.setSafeOnClickListener {
                startActivity(ClientDetailForInvoiceActivity.newIntent(this@ClientListForInvoiceActivity))
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ClientListForInvoiceActivity::class.java)
    }
}
