package com.koinvois.generator.ui.reports.subfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.koinvois.generator.R
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.databinding.FragmentPaidReportBinding
import com.koinvois.generator.ui.reports.ReportsMainViewModel
import com.koinvois.generator.ui.reports.adapter.PaidReportAdapter
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.launch


class PaidReportFragment : Fragment() {

    private var binding: FragmentPaidReportBinding? = null
    private val viewModel: ReportsMainViewModel by hiltNavGraphViewModels(R.id.report_navigation_graph)
    private var paidReportAdapter: PaidReportAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaidReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        observeData()
    }

    private fun setRecyclerView() {
        paidReportAdapter = PaidReportAdapter()
        binding?.rvPaidInvoices?.adapter = paidReportAdapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paidInvoices.collect { list ->
                    if (list.isNotEmpty()) {
                        paidReportAdapter?.submitList(list)
                        
                        // Update summary cards with real data from database
                        binding?.txtTotalInvoices?.text = list.size.toString()
                        val totalPaid = list.sumOf { (it.invoiceTotal ?: 0f).toDouble() }
                        binding?.txtTotalPaid?.text = CurrencyFormatter.format(totalPaid)

                        binding?.rvPaidInvoices?.visible()
                    } else {
                        binding?.txtTotalInvoices?.text = "0"
                        binding?.txtTotalPaid?.text = CurrencyFormatter.format(0.0)
                        binding?.rvPaidInvoices?.hide()
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