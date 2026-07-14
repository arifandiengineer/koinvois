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

        lifecycleScope.launch(Dispatchers.Main)
        {
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
            AllClientsForInvoiceAdapter(allClients, viewModel) { finish() }
    }

    private fun setUpToolbar() {
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_clients)
        binding.customToolbar.btnBack.setSafeOnClickListener {
            finish()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ClientListForInvoiceActivity::class.java)
    }
}
