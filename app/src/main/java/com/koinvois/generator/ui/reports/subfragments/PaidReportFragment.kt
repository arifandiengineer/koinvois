package com.koinvois.generator.ui.reports.subfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentPaidReportBinding
import com.koinvois.generator.ui.reports.ReportsMainViewModel
import com.koinvois.generator.ui.reports.adapter.PaidReportAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible


class PaidReportFragment : Fragment() {

    private var binding: FragmentPaidReportBinding? = null
    private val viewModel: ReportsMainViewModel by hiltNavGraphViewModels(R.id.report_navigation_graph)
    private var paidReportAdapter: PaidReportAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaidReportBinding.inflate(inflater, container, false)

        setRecyclerView()
        viewModel.getPaidInvoicesReport()

        return binding?.root
    }

    private fun setRecyclerView() {
        viewModel.paidInvoicesLive.observe(viewLifecycleOwner) { list ->

            if (!list.isNullOrEmpty()) {
                val adapter = paidReportAdapter ?: PaidReportAdapter().also {
                    paidReportAdapter = it
                    binding?.rvPaidInvoices?.adapter = it
                }
                adapter.submitList(list)
                
                // Update summary cards with real data
                binding?.txtTotalInvoices?.text = list.size.toString()
                val totalPaid = list.sumOf { (it.invoiceTotal ?: 0f).toDouble() }
                binding?.txtTotalPaid?.text = "$${String.format("%.2f", totalPaid)}"
                if (list.isNotEmpty()) {
                    binding?.txtAvgInvoice?.text = "$${String.format("%.2f", totalPaid / list.size)}"
                }
                binding?.txtTotalClients?.text = list.map { it.invoiceClientName }.distinct().size.toString()

                binding?.rvPaidInvoices?.visible()
            } else {
                binding?.rvPaidInvoices?.hide()
            }
        }
    }
}