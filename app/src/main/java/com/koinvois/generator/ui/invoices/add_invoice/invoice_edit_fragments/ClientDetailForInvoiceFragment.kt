package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.FragmentClientDetailForInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.extensions.*

class ClientDetailForInvoiceFragment : Fragment() {

    private var binding: FragmentClientDetailForInvoiceBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientDetailForInvoiceBinding.inflate(inflater, container, false)

        setToolbar()
        backPressed()
        return binding?.root
    }

    private fun setToolbar() {
        binding?.btnBack?.setSafeOnClickListener {
            saveOnBack()
        }
        
        binding?.btnSave?.setSafeOnClickListener {
            saveOnBack()
        }

        // Title and subtitle are already set in XML, but we can update if needed
        binding?.txtHeaderTitle?.text = getString(R.string.more_menu_client)
    }

    private fun saveOnBack() {
        if (binding?.editClientName?.getString()?.isNotEmpty() == true) {
            binding?.let {
                with(it) {
                    // Mobile Number
                    var mobileStr = editMobileNumber.getString()?.trim()
                    if (mobileStr != null && mobileStr.startsWith("+")) {
                        mobileStr = mobileStr.substring(1)
                    }
                    val mobileInt = mobileStr?.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                    // Phone Number
                    var phoneStr = editPhoneNumber.getString()?.trim()
                    if (phoneStr != null && phoneStr.startsWith("+")) {
                        phoneStr = phoneStr.substring(1)
                    }
                    val phoneInt = phoneStr?.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                    // Fax Number
                    var faxStr = editFaxNumber.getString()?.trim()
                    if (faxStr != null && faxStr.startsWith("+")) {
                        faxStr = faxStr.substring(1)
                    }
                    val faxInt = faxStr?.takeIf { it.isDigitsOnly() }?.toIntOrNull()

                    viewModel.selectedClient =
                        Client(
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
                }
            }
            findNavController().navigateUp()
        } else {
            Toast.makeText(activity, getString(R.string.error_enter_client_name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.let { bin ->
            with(bin)
            {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
