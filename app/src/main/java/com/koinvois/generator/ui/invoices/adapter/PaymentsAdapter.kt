package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.data_models.InvoicePaymentsModel
import com.koinvois.generator.databinding.ItemPaymentBinding

class PaymentsAdapter(private val paymentsList: ArrayList<InvoicePaymentsModel>) : RecyclerView.Adapter<PaymentsAdapter.PaymentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsViewHolder {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int) {
        paymentsList[position].let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return paymentsList.size
    }

    class PaymentsViewHolder(val binding: ItemPaymentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: InvoicePaymentsModel) {
            binding.paymentAmount.text = payment.amount?.toString() ?: "0.00"
            binding.paymentDate.text = payment.paymentDate ?: "un-defined"
            binding.txtPaymentNotes.text = payment.paymentNotes ?: ""
        }
    }

}