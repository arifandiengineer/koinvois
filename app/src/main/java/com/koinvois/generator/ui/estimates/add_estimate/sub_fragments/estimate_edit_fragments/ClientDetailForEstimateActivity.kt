package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.ActivityEstimateClientDetailBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientDetailForEstimateActivity : BaseActivity<ActivityEstimateClientDetailBinding>() {

    private val viewModel: EstimatesMainViewModel by viewModels()

    override fun inflateBinding(): ActivityEstimateClientDetailBinding =
        ActivityEstimateClientDetailBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }
    }

    private fun setUpToolbar() {
        binding.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.btnSave.setSafeOnClickListener {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding.editClientName.getString()?.isNotEmpty() == true) {
            with(binding) {
                viewModel.selectedClient =
                    Client(
                        viewModel.selectedClient?.clientId ?: 0,
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
            finish()
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
        fun newIntent(context: Context): Intent = Intent(context, ClientDetailForEstimateActivity::class.java)
    }
}
