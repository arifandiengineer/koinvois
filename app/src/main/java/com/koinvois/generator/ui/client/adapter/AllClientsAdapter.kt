package com.koinvois.generator.ui.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.databinding.ItemClientBinding

class AllClientsAdapter(private val onClientClick: (Client) -> Unit) :
    BaseRecyclerAdapter<Client, ItemClientBinding>(ClientDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemClientBinding =
        ItemClientBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemClientBinding, item: Client, position: Int) {
        binding.txtClientName.text = item.clientName

        binding.root.setOnClickListener {
            onClientClick(item)
        }
    }

    class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean =
            oldItem.clientId == newItem.clientId

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean =
            oldItem == newItem
    }
}
