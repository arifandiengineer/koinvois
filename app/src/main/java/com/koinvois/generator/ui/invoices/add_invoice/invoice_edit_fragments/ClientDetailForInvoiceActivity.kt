package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.ActivityInvoiceClientDetailBinding
import com.koinvois.generator.ui.client.ClientMainViewModel
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientDetailForInvoiceActivity : BaseActivity<ActivityInvoiceClientDetailBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()
    private val clientViewModel: ClientMainViewModel by viewModels()

    override fun inflateBinding(): ActivityInvoiceClientDetailBinding =
        ActivityInvoiceClientDetailBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setToolbar()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }
    }

    private fun setToolbar() {
        binding.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.btnSave.setSafeOnClickListener {
            saveOnBack()
        }

        binding.txtHeaderTitle.text = getString(R.string.more_menu_client)
    }

    private fun saveOnBack() {
        if (binding.editClientName.getString()?.isNotEmpty() == true) {
            lifecycleScope.launch(Dispatchers.Main) {
                with(binding) {
                    var mobileStr = editMobileNumber.getString()?.trim()
                    if (mobileStr != null && mobileStr.startsWith("+")) {
                        mobileStr = mobileStr.substring(1)
                    }
                    val mobileInt = mobileStr?.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                    var phoneStr = editPhoneNumber.getString()?.trim()
                    if (phoneStr != null && phoneStr.startsWith("+")) {
                        phoneStr = phoneStr.substring(1)
                    }
                    val phoneInt = phoneStr?.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                    var faxStr = editFaxNumber.getString()?.trim()
                    if (faxStr != null && faxStr.startsWith("+")) {
                        faxStr = faxStr.substring(1)
                    }
                    val faxInt = faxStr?.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                    val client = Client(
                        viewModel.selectedClient?.clientId ?: 0,
                        editClientName.getString(),
                        editClientEmail.getString(),
                        mobileInt,
                        phoneInt,
                        faxInt,
                        editContact.getString(),
                        editAddress1.getString(),
                        editAddress2.getString(),
                        editAddress3.getString()
                    )

                    // Save to database as well so it appears in ClientList
                    clientViewModel.addClient(client.toDomain())
                    
                    // Also set as selected for the current invoice
                    viewModel.selectedClient = client
                }
                finish()
            }
        } else {
            Toast.makeText(this, getString(R.string.error_enter_client_name), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            editClientName.setText(viewModel.selectedClient?.clientName)
            editClientEmail.setText(viewModel.selectedClient?.clientEmail)
            editMobileNumber.setText(viewModel.selectedClient?.clientMobile?.toString() ?: "")
            editPhoneNumber.setText(viewModel.selectedClient?.clientPhone?.toString() ?: "")
            editFaxNumber.setText(viewModel.selectedClient?.clientFax?.toString() ?: "")
            editContact.setText(viewModel.selectedClient?.clientContact)
            editAddress1.setText(viewModel.selectedClient?.clientAddress1)
            editAddress2.setText(viewModel.selectedClient?.clientAddress2)
            editAddress3.setText(viewModel.selectedClient?.clientAddress3)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ClientDetailForInvoiceActivity::class.java)
    }
}
