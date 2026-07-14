package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.FragmentClientListForEstimateBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.AllClientsForEstimateAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientListForEstimateFragment : Fragment() {

    private var binding: FragmentClientListForEstimateBinding? = null
    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientListForEstimateBinding.inflate(inflater, container, false)

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
            AllClientsForEstimateAdapter(allClients, findNavController(), viewModel)
    }

    private fun setUpToolbar() {
        binding?.let {
            with(it) {
                customToolbar.btnBack.visible()
                customToolbar.imgSecondaryAction.inVisible()
                customToolbar.imgRightAction.inVisible()
                customToolbar.txtToolbarTitle.text = getString(R.string.title_clients)
                customToolbar.btnBack.setSafeOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }
    }
}