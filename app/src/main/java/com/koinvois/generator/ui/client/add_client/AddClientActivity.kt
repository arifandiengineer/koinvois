package com.koinvois.generator.ui.client.add_client

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.databinding.ActivityClientAddBinding
import com.koinvois.generator.ui.client.ClientMainViewModel
import com.koinvois.generator.utilities.constants.Constants
import com.koinvois.generator.utilities.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddClientActivity : BaseActivity<ActivityClientAddBinding>() {

    private val viewModel: ClientMainViewModel by viewModels()
    private val clientId: Int by lazy { intent.getIntExtra(EXTRA_CLIENT_ID, -1) }
    private val clientType: String get() = if (clientId != -1) Constants.EXISTING_CLIENT else Constants.NEW_CLIENT
    private var client: Client? = null

    override fun inflateBinding(): ActivityClientAddBinding =
        ActivityClientAddBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setToolbar()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }

        if (clientType == Constants.EXISTING_CLIENT) {
            lifecycleScope.launch(Dispatchers.Main) {
                client = viewModel.getClientById(clientId)
                populateFields(client)
            }
        }
    }

    private fun populateFields(client: Client?) {
        client ?: return
        with(binding) {
            editClientName.setText(client.clientName)
            editContact.setText(client.clientContact)
            editClientEmail.setText(client.clientEmail)
            editFaxNumber.setText(client.clientFax?.toString())
            editMobileNumber.setText(client.clientMobile?.toString())
            editPhoneNumber.setText(client.clientPhone?.toString())
            editAddress1.setText(client.clientAddress1)
            editAddress2.setText(client.clientAddress2)
            editAddress3.setText(client.clientAddress3)
        }
    }

    private fun setToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = if (clientType == Constants.EXISTING_CLIENT)
            getString(R.string.label_edit_client) else getString(R.string.label_add_client)

        if (clientType == Constants.EXISTING_CLIENT) {
            binding.customToolbar.imgRightAction.visible()
            binding.customToolbar.imgRightAction.setImageResource(R.drawable.icon_three_dot)
            binding.customToolbar.imgRightAction.setSafeOnClickListener {
                val popup = PopupMenu(this, it)
                popup.menuInflater.inflate(R.menu.popup_menu_client, popup.menu)
                popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.title) {
                            getString(R.string.delete_menu_item) -> {
                                BaseDialog.confirm(
                                    context = this@AddClientActivity,
                                    title = getString(R.string.delete_confirm_title),
                                    message = getString(R.string.delete_client_confirm_message),
                                    positiveText = getString(R.string.delete_confirm_positive),
                                    negativeText = getString(R.string.delete_confirm_negative),
                                    onConfirm = {
                                        lifecycleScope.launch(Dispatchers.Main) {
                                            client?.let { it1 -> viewModel.deleteClient(it1) }
                                            finish()
                                        }
                                    }
                                )
                            }
                        }
                        return true
                    }
                })
                popup.show()
            }
        }

        binding.customToolbar.btnBack.setOnClickListener {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding.editClientName.text.toString().isNotEmpty()) {
            when (clientType) {
                Constants.NEW_CLIENT -> {
                    val newClient = with(binding) {
                        Client(
                            0,
                            editClientName.getString(),
                            editClientEmail.getString(),
                            editMobileNumber.getInt(),
                            editPhoneNumber.getInt(),
                            editFaxNumber.getInt(),
                            editContact.getString(),
                            editAddress1.getString(),
                            editAddress2.getString(),
                            editAddress3.getString()
                        )
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        viewModel.addClient(newClient)
                    }
                }
                Constants.EXISTING_CLIENT -> {
                    val updatedClient = client?.clientId?.let { id ->
                        with(binding) {
                            Client(
                                id,
                                editClientName.getString(),
                                editClientEmail.getString(),
                                editMobileNumber.getInt(),
                                editPhoneNumber.getInt(),
                                editFaxNumber.getInt(),
                                editContact.getString(),
                                editAddress1.getString(),
                                editAddress2.getString(),
                                editAddress3.getString()
                            )
                        }
                    }
                    updatedClient?.let { it1 ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.updateClient(it1)
                        }
                    }
                }
            }
            finish()
        } else {
            finish()
        }
    }

    companion object {
        private const val EXTRA_CLIENT_ID = "extra_client_id"

        fun newIntent(context: Context, clientId: Int = -1): Intent =
            Intent(context, AddClientActivity::class.java).apply {
                putExtra(EXTRA_CLIENT_ID, clientId)
            }
    }
}
