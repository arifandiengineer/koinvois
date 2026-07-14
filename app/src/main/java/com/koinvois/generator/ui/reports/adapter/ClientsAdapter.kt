package com.koinvois.generator.ui.reports.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.databinding.ItemPaidClientsBinding
import com.koinvois.generator.ui.reports.ReportsMainViewModel

class ClientsAdapter : BaseRecyclerAdapter<ReportsMainViewModel.ClientReportModel, ItemPaidClientsBinding>(
    ClientReportDiffCallback()
) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPaidClientsBinding {
        return ItemPaidClientsBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemPaidClientsBinding, item: ReportsMainViewModel.ClientReportModel, position: Int) {
        Log.e("report", item.client)

        binding.txtClientName.text = item.client
        binding.txtInitials.text = item.client.take(2).uppercase()
        binding.txtPaidAmount.text = CurrencyFormatter.format(item.invoiceAmount)
        binding.txtInvoices.text = item.invoicesCount.toString()
    }

    class ClientReportDiffCallback : DiffUtil.ItemCallback<ReportsMainViewModel.ClientReportModel>() {
        override fun areItemsTheSame(
            oldItem: ReportsMainViewModel.ClientReportModel,
            newItem: ReportsMainViewModel.ClientReportModel
        ): Boolean = oldItem.client == newItem.client

        override fun areContentsTheSame(
            oldItem: ReportsMainViewModel.ClientReportModel,
            newItem: ReportsMainViewModel.ClientReportModel
        ): Boolean = oldItem == newItem
    }
}
