package com.koinvois.generator.ui.reports.subfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentClientsReportBinding
import com.koinvois.generator.ui.reports.ReportsMainViewModel
import com.koinvois.generator.ui.reports.adapter.ClientsAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.launch

class ClientsReportFragment : Fragment() {

    private var binding: FragmentClientsReportBinding? = null
    private val viewModel: ReportsMainViewModel by hiltNavGraphViewModels(R.id.report_navigation_graph)
    private var clientsReportAdapter: ClientsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientsReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        observeData()
    }

    private fun setRecyclerView() {
        clientsReportAdapter = ClientsAdapter()
        binding?.rvClientsReport?.adapter = clientsReportAdapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clientsReport.collect { list ->
                    if (list.isNotEmpty()) {
                        clientsReportAdapter?.submitList(list)
                        binding?.rvClientsReport?.visible()
                    } else {
                        binding?.rvClientsReport?.hide()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}