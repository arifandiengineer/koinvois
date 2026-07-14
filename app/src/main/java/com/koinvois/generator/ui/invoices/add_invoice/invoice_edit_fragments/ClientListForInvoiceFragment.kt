package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.FragmentClientListForInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.AllClientsForInvoiceAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientListForInvoiceFragment : Fragment() {

    private var binding: FragmentClientListForInvoiceBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientListForInvoiceBinding.inflate(inflater, container, false)

        setUpToolbar()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
        {
            val allClients = viewModel.allClients

            if (allClients?.isEmpty() == true) {
                binding?.txtName?.hide()
                binding?.rvAllClients?.hide()
            } else {
                binding?.txtName?.visible()
                binding?.rvAllClients?.visible()
                allClients?.let { setUpRecyclerView(it) }
            }
        }

        return binding?.root
    }

    private fun setUpRecyclerView(allClients: ArrayList<Client>) {
        binding?.rvAllClients?.adapter =
            AllClientsForInvoiceAdapter(allClients, findNavController(), viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setUpToolbar() {
        binding?.let {
            with(it) {
                customToolbar.txtToolbarTitle.text = getString(R.string.title_clients)
                customToolbar.btnBack.setSafeOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }
    }
}