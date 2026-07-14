package com.koinvois.generator.ui.invoices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.database.models.Invoice
import com.koinvois.generator.databinding.InvoiceMainFragmentBinding
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainActivity
import com.koinvois.generator.ui.invoices.adapter.ViewPagerAdapter
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.widget.addTextChangedListener

@AndroidEntryPoint
class InvoiceMainFragment : Fragment() {

    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)
    private var _binding: InvoiceMainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InvoiceMainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        observeData()
        setClickListeners()
    }


    private fun observeData() {
        viewModel.allInvoicesLive.observe(viewLifecycleOwner) { invoices ->
            updateSummary(invoices)
            updateTabLabels(invoices)
        }
    }

    private fun updateSummary(invoices: List<Invoice>) {
        val totalCount = invoices.size
        val outstandingAmount = invoices.filter { it.invoiceStatus == InvoiceStatusEnum.UN_PAID.status }
            .sumOf { (it.invoiceTotal ?: 0f).toDouble() }
        val paidAmount = invoices.filter { it.invoiceStatus == InvoiceStatusEnum.PAID.status }
            .sumOf { (it.invoiceTotal ?: 0f).toDouble() }

        binding.cardTotalInvoices.apply {
            txtLabel.text = getString(R.string.label_total_invoices)
            txtValue.text = totalCount.toString()
            imgIcon.setImageResource(R.drawable.invoice_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_green_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_success))
            txtGrowth.text = "+12%" 
            imgGrowthIcon.setImageResource(R.drawable.ic_arrow_growth_up)
        }

        binding.cardOutstanding.apply {
            txtLabel.text = getString(R.string.label_outstanding)
            txtValue.text = CurrencyFormatter.format(outstandingAmount)
            imgIcon.setImageResource(R.drawable.estimate_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_orange_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_stat_orange))
            txtGrowth.text = "+5%"
            txtGrowth.setTextColor(requireContext().getColor(R.color.color_stat_orange))
            imgGrowthIcon.setImageResource(R.drawable.ic_arrow_growth_up)
            imgGrowthIcon.setColorFilter(requireContext().getColor(R.color.color_stat_orange))
        }

        binding.cardPaid.apply {
            txtLabel.text = getString(R.string.label_paid_summary)
            txtValue.text = CurrencyFormatter.format(paidAmount)
            imgIcon.setImageResource(R.drawable.business_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_green_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_success))
            txtGrowth.text = "+18%"
            imgGrowthIcon.setImageResource(R.drawable.ic_arrow_growth_up)
        }
    }

    private fun updateTabLabels(invoices: List<Invoice>) {
        val allCount = invoices.size
        val outstandingCount = invoices.count { it.invoiceStatus == InvoiceStatusEnum.UN_PAID.status }
        val paidCount = invoices.count { it.invoiceStatus == InvoiceStatusEnum.PAID.status }

        binding.tabLayout.getTabAt(0)?.text = "All ($allCount)"
        binding.tabLayout.getTabAt(1)?.text = "Outstanding ($outstandingCount)"
        binding.tabLayout.getTabAt(2)?.text = "Paid ($paidCount)"
    }

    private fun setClickListeners() {
        binding.btnAddInvoice.setSafeOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.prepareNewInvoice()
                startActivity(AddInvoiceMainActivity.newIntent(requireContext(), DBEnum.NEW.entryType))
            }
        }
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.visible()
        binding.tabLayout.visible()

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Outstanding"
                2 -> "Paid"
                else -> "All"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
