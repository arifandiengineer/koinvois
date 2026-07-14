package com.koinvois.generator.ui.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.databinding.ItemClientBinding
import com.koinvois.generator.ui.client.ClientMainFragmentDirections
import com.koinvois.generator.ui.client.ClientMainViewModel
import com.koinvois.generator.utilities.constants.Constants

class AllClientsAdapter(private val navController: NavController, val viewModel: ClientMainViewModel) :
    BaseRecyclerAdapter<Client, ItemClientBinding>(ClientDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemClientBinding =
        ItemClientBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemClientBinding, item: Client, position: Int) {
        binding.txtClientName.text = item.clientName

        binding.root.setOnClickListener {
            viewModel.clientUpdateModel.postValue(item)
            val action = ClientMainFragmentDirections.actionMainClientToAddClient(Constants.EXISTING_CLIENT)
            navController.navigate(action)
        }
    }

    class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean =
            oldItem.clientId == newItem.clientId

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean =
            oldItem == newItem
    }
}
