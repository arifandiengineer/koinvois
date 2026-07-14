package com.koinvois.generator.ui.dashboard

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentDashboardBinding
import com.koinvois.generator.ui.client.ClientMainActivity
import com.koinvois.generator.ui.dashboard.adapter.RecentInvoiceAdapter
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.item.ItemMainActivity
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.getScreenWidth
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val invoiceViewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    private var recentInvoiceAdapter: RecentInvoiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeSummary()
        observeRecentInvoices()
        setClickListeners()
        backPressed()
    }

    private fun setupAdapter() {
        recentInvoiceAdapter = RecentInvoiceAdapter { invoice ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                invoiceViewModel.loadInvoiceById(invoice.invoiceId)
                val action = DashboardFragmentDirections.actionFragmentDashboardMainToFragmentEditInvoiceMain(
                    invoiceType = DBEnum.OLD.entryType,
                    invoiceId = invoice.invoiceId
                )
                findNavController().navigate(action)
            }
        }
        binding.rvRecentInvoices.adapter = recentInvoiceAdapter
    }

    private fun observeSummary() {
        viewModel.summary.observe(viewLifecycleOwner) { summary ->
            with(binding) {
                txtTotalInvoices.text = summary.totalInvoices.toString()
                txtTotalInvoicesTrend.text = summary.totalInvoicesTrend
                
                txtRevenuePaid.text = String.format(Locale.getDefault(), "$%.2f", summary.totalRevenuePaid)
                txtRevenueTrend.text = summary.revenueTrend
                
                txtOutstanding.text = String.format(Locale.getDefault(), "$%.2f", summary.totalOutstandingAmount)
                txtOutstandingTrend.text = summary.outstandingTrend
                
                txtOpenEstimates.text = summary.openEstimateCount.toString()
                txtEstimatesTrend.text = summary.estimatesTrend
            }
        }
    }

    private fun observeRecentInvoices() {
        viewModel.recentInvoices.observe(viewLifecycleOwner) { invoices ->
            if (invoices.isNotEmpty()) {
                recentInvoiceAdapter?.submitList(invoices)
                binding.rvRecentInvoices.visible()
                binding.txtNoRecentInvoices.hide()
            } else {
                binding.rvRecentInvoices.hide()
                binding.txtNoRecentInvoices.visible()
            }
        }
    }

    private fun setClickListeners() {
        binding.btnQuickNewInvoice.setSafeOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                invoiceViewModel.prepareNewInvoice()
                val action = DashboardFragmentDirections.actionFragmentDashboardMainToFragmentEditInvoiceMain(
                    invoiceType = DBEnum.NEW.entryType
                )
                findNavController().navigate(action)
            }
        }
        binding.btnQuickNewEstimate.setSafeOnClickListener {
            findNavController().navigate(R.id.estimate_navigation_graph)
        }
        binding.btnQuickAddClient.setSafeOnClickListener {
            startActivity(ClientMainActivity.newIntent(requireContext()))
        }
        binding.btnQuickAddItem.setSafeOnClickListener {
            startActivity(ItemMainActivity.newIntent(requireContext()))
        }
        binding.btnViewAll.setSafeOnClickListener {
            findNavController().navigate(R.id.invoice_navigation_graph)
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitDialog()
                }
            },
        )
    }

    private fun showExitDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.exit_dialog)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.transparentColor)))
        dialog.window?.setLayout(
            requireContext().getScreenWidth(),
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        dialog.show()
        
        dialog.findViewById<TextView>(R.id.btnOkExit)?.setSafeOnClickListener {
            activity?.moveTaskToBack(true)
            activity?.finish()
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.btnCancelExit)?.setSafeOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        recentInvoiceAdapter = null
    }
}
