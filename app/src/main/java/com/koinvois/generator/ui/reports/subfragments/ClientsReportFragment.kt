package com.koinvois.generator.ui.reports.subfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentClientsReportBinding
import com.koinvois.generator.ui.reports.ReportsMainViewModel
import com.koinvois.generator.ui.reports.adapter.ClientsAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible

class ClientsReportFragment : Fragment() {

    private var binding: FragmentClientsReportBinding? = null
    private val viewModel: ReportsMainViewModel by hiltNavGraphViewModels(R.id.report_navigation_graph)
    private var clientsReportAdapter: ClientsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientsReportBinding.inflate(inflater, container, false)

        setRecyclerView()
        viewModel.getClientReport()

        return binding?.root
    }

    private fun setRecyclerView() {
        viewModel.clientReportLoaded.observe(viewLifecycleOwner) {

            if (viewModel.clientsReportList.size > 0) {
                val adapter = clientsReportAdapter ?: ClientsAdapter().also {
                    clientsReportAdapter = it
                    binding?.rvClientsReport?.adapter = it
                }
                adapter.submitList(viewModel.clientsReportList.toList())
                binding?.rvClientsReport?.visible()
            } else {
                binding?.rvClientsReport?.hide()
            }
        }
    }
}