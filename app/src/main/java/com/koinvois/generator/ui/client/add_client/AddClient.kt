package com.koinvois.generator.ui.client.add_client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.koinvois.generator.R
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.databinding.AddClientFragmentBinding
import com.koinvois.generator.ui.client.ClientMainViewModel
import com.koinvois.generator.utilities.constants.Constants
import com.koinvois.generator.utilities.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddClient : Fragment() {

    private var binding: AddClientFragmentBinding? = null
    private val viewModel: ClientMainViewModel by hiltNavGraphViewModels(R.id.client_navigation_graph)
    private val args: AddClientArgs by navArgs()
    private var client: Client? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddClientFragmentBinding.inflate(inflater, container, false)
        Log.e("type", args.clientType)

        if (args.clientType == Constants.EXISTING_CLIENT) {
            binding?.let {
                with(it) {
                    with(viewModel.clientUpdateModel.value) {
                        this?.let {
                            editClientName.setText(clientName)
                            editContact.setText(clientContact)
                            editClientEmail.setText(clientEmail)
                            editFaxNumber.setText(clientFax?.toString())
                            editMobileNumber.setText(clientMobile?.toString())
                            editPhoneNumber.setText(clientPhone?.toString())
                            editAddress1.setText(clientAddress1)
                            editAddress2.setText(clientAddress2)
                            editAddress3.setText(clientAddress3)
                        }
                    }
                }
            }
        }
        setToolbar()
        backPressed()
        return binding?.root
    }

    private fun setToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = if (args.clientType == Constants.EXISTING_CLIENT) 
            getString(R.string.label_edit_client) else getString(R.string.label_add_client)

        if (args.clientType == Constants.EXISTING_CLIENT) {
            binding?.customToolbar?.imgRightAction?.visible()
            binding?.customToolbar?.imgRightAction?.setImageResource(R.drawable.icon_three_dot)
            binding?.customToolbar?.imgRightAction?.setSafeOnClickListener {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.popup_menu_client, popup.menu)
                popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.title) {
                            getString(R.string.delete_menu_item) -> {
                                context?.let { ctx ->
                                    BaseDialog.confirm(
                                        context = ctx,
                                        title = getString(R.string.delete_confirm_title),
                                        message = getString(R.string.delete_client_confirm_message),
                                        positiveText = getString(R.string.delete_confirm_positive),
                                        negativeText = getString(R.string.delete_confirm_negative),
                                        onConfirm = {
                                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                                viewModel.deleteClient()
                                                findNavController().navigateUp()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        return true
                    }
                })
                popup.show()
            }
        }

        binding?.customToolbar?.btnBack?.setOnClickListener {
            saveOnBack()
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding?.editClientName?.text.toString().isNotEmpty()) {
            when (args.clientType) {
                Constants.NEW_CLIENT -> {
                    binding?.let {
                        with(it) {
                            client = Client(
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
                    }
                    client?.let { it1 ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.addClient(it1)
                        }
                    }
                }
                Constants.EXISTING_CLIENT -> {
                    binding?.let {
                        with(it) {
                            client = viewModel.clientUpdateModel.value?.clientId?.let { it1 ->
                                Client(
                                    it1,
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
                    }
                    client?.let { it1 ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.updateClient(it1)
                        }
                    }

                    Log.e("client", client?.clientId.toString())
                }
            }
            findNavController().navigateUp()
        } else {
            findNavController().navigateUp()
        }
    }
}