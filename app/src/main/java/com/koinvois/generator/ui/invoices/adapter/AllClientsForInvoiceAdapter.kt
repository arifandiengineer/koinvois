package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.databinding.ItemClientInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel

class AllClientsForInvoiceAdapter(
    private val clientList: ArrayList<Client>,
    val viewModel: InvoiceMainViewModel,
    private val onClientClick: (Client) -> Unit
) :
    RecyclerView.Adapter<AllClientsForInvoiceAdapter.AllClientsForInvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClientsForInvoiceViewHolder {
        val binding = ItemClientInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllClientsForInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllClientsForInvoiceViewHolder, position: Int) {
        holder.bind(clientList[position])

        holder.itemView.setOnClickListener {
            viewModel.selectedClient = clientList[position]
            onClientClick(clientList[position])
        }
    }

    override fun getItemCount(): Int {
        return clientList.size
    }

    class AllClientsForInvoiceViewHolder(private val binding: ItemClientInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(client: Client) {
            binding.txtClientName.text = client.clientName
        }
    }
}