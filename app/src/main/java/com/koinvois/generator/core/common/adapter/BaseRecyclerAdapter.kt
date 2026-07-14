package com.koinvois.generator.core.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * ListAdapter + DiffUtil wrapper so every RecyclerView list gets
 * async diffing for free instead of notifyDataSetChanged().
 *
 * Usage:
 * ```
 * class ClientAdapter(onClick: (Client) -> Unit) :
 *     BaseRecyclerAdapter<Client, ItemClientBinding>(ClientDiffCallback()) {
 *     override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup) =
 *         ItemClientBinding.inflate(inflater, parent, false)
 *     override fun onBind(binding: ItemClientBinding, item: Client, position: Int) { ... }
 * }
 * ```
 */
abstract class BaseRecyclerAdapter<T : Any, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseRecyclerAdapter.BaseViewHolder<VB>>(diffCallback) {

    protected abstract fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): VB
    protected abstract fun onBind(binding: VB, item: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = inflateBinding(LayoutInflater.from(parent.context), parent)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        onBind(holder.binding, getItem(position), position)
    }

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
