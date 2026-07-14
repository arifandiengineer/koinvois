package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.FragmentClientDetailForEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*

class ClientDetailForEstimateFragment : Fragment() {

    private var binding: FragmentClientDetailForEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientDetailForEstimateBinding.inflate(inflater, container, false)

        setUpToolbar()
        backPressed()

        return binding?.root
    }

    private fun setUpToolbar() {
        binding?.btnBack?.setSafeOnClickListener {
            saveOnBack()
        }
        
        binding?.btnSave?.setSafeOnClickListener {
            saveOnBack()
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding?.editClientName?.getString()?.isNotEmpty() == true) {
            binding?.let {
                with(it) {
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
            }
            findNavController().navigateUp()
        } else {
            Toast.makeText(activity, getString(R.string.error_enter_client_name), Toast.LENGTH_SHORT).show()
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
