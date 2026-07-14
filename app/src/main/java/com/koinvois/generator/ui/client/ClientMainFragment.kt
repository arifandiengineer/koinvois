package com.koinvois.generator.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.MainActivity
import com.koinvois.generator.R
import com.koinvois.generator.core.common.state.bind
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.databinding.ClientMainFragmentBinding
import com.koinvois.generator.ui.client.adapter.AllClientsAdapter
import com.koinvois.generator.utilities.constants.Constants
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientMainFragment : Fragment() {

    private var binding: ClientMainFragmentBinding? = null
    private val viewModel: ClientMainViewModel by hiltNavGraphViewModels(R.id.client_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ClientMainFragmentBinding.inflate(inflater, container, false)

        setUpToolbar()
        setClickListeners()
//        setUpViewPager()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
        {
            val allClients = viewModel.getAllClients()
            if (allClients.isEmpty()) {
                binding?.txtName?.hide()
                binding?.txtTotalBilled?.hide()
                binding?.rvAllClients?.hide()
                binding?.emptyState?.root?.visible()
                binding?.emptyState?.bind(title = getString(R.string.no_clients_empty_state))

            } else {
                binding?.txtName?.visible()
                binding?.txtTotalBilled?.visible()
                binding?.rvAllClients?.visible()
                binding?.emptyState?.root?.hide()
                setUpRecyclerView(allClients)
            }
        }

        return binding?.root
    }

    private fun setUpRecyclerView(allClients: ArrayList<Client>) {
        val adapter = AllClientsAdapter(findNavController(), viewModel)
        binding?.rvAllClients?.adapter = adapter
        adapter.submitList(allClients)
    }

    private fun setUpToolbar() {
        val customToolbar = (activity as MainActivity).binding?.customToolbar
        customToolbar?.imgRightAction?.visible()
        customToolbar?.btnBack?.inVisible()
        customToolbar?.imgSecondaryAction?.inVisible()
        customToolbar?.txtToolbarTitle?.text = getString(R.string.title_clients)
    }

    private fun setClickListeners() {
        binding?.btnAddClient?.setOnClickListener {
            val action = ClientMainFragmentDirections.actionMainClientToAddClient(Constants.NEW_CLIENT)
            findNavController().navigate(action)
        }
    }
}