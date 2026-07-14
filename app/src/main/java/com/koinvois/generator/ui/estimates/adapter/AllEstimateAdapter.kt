package com.koinvois.generator.ui.estimates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.R
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.database.models.Estimate
import com.koinvois.generator.databinding.ItemEstimateListBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllEstimateAdapter(
    val viewModel: EstimatesMainViewModel,
    val lifecycleOwner: LifecycleOwner,
    private val onEstimateClick: (Estimate) -> Unit
) : BaseRecyclerAdapter<Estimate, ItemEstimateListBinding>(EstimateDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemEstimateListBinding =
        ItemEstimateListBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemEstimateListBinding, item: Estimate, position: Int) {
        binding.txtClientName.text = item.estimateClientName ?: "No Client"
        binding.txtInitials.text = item.estimateClientName?.take(2)?.uppercase() ?: "NA"
        
        binding.txtEstimateIdAndDate.text = "EST-${item.estimateNumber ?: item.estimateId} • ${item.estimateDate ?: "No Date"}"
        binding.txtValidUntil.text = "Valid until: ${item.estimateDate ?: "No Date"}"
        binding.txtAmount.text = CurrencyFormatter.format(item.estimateTotal ?: 0f)

        val context = binding.root.context
        when (item.estimateStatus) {
            EstimateStatusEnum.OPEN.status -> {
                binding.txtStatusBadge.text = context.getString(R.string.status_open)
                binding.txtStatusBadge.visible()
                binding.txtStatusBadge.backgroundTintList = android.content.res.ColorStateList.valueOf(context.getColor(R.color.color_stat_orange_bg))
                binding.txtStatusBadge.setTextColor(context.getColor(R.color.color_stat_orange))
            }
            EstimateStatusEnum.CLOSED.status -> {
                binding.txtStatusBadge.text = context.getString(R.string.status_closed)
                binding.txtStatusBadge.visible()
                binding.txtStatusBadge.backgroundTintList = android.content.res.ColorStateList.valueOf(context.getColor(R.color.color_stat_green_bg))
                binding.txtStatusBadge.setTextColor(context.getColor(R.color.color_success))
            }
            else -> binding.txtStatusBadge.hide()
        }

        binding.root.setSafeOnClickListener {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.loadViewModelData(item)
                onEstimateClick(item)
            }
        }
    }

    class EstimateDiffCallback : DiffUtil.ItemCallback<Estimate>() {
        override fun areItemsTheSame(oldItem: Estimate, newItem: Estimate): Boolean =
            oldItem.estimateId == newItem.estimateId

        override fun areContentsTheSame(oldItem: Estimate, newItem: Estimate): Boolean =
            oldItem.copy(estimateSignature = null) == newItem.copy(estimateSignature = null)
    }
}
