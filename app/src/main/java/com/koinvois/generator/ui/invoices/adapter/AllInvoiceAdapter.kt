package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import com.koinvois.generator.R
import com.koinvois.generator.core.common.adapter.BaseRecyclerAdapter
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.database.models.Invoice
import com.koinvois.generator.databinding.ItemClientBinding
import com.koinvois.generator.ui.invoices.InvoiceMainFragmentDirections
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import com.koinvois.generator.databinding.ItemInvoiceBinding

class AllInvoiceAdapter(
    val viewModel: InvoiceMainViewModel,
    val navController: NavController,
    val lifecycleOwner: LifecycleOwner
) : BaseRecyclerAdapter<Invoice, ItemInvoiceBinding>(InvoiceDiffCallback()) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemInvoiceBinding =
        ItemInvoiceBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemInvoiceBinding, item: Invoice, position: Int) {
        val context = binding.root.context
        
        binding.txtClientName.text = item.invoiceClientName ?: "No Client"
        
        // Initials
        val name = item.invoiceClientName ?: "No Client"
        val initials = name.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
        binding.txtInitials.text = initials

        // Invoice ID and Date
        val invoiceIdStr = "INV-${item.invoiceDate?.replace("-", "") ?: "0000"}-${item.invoiceId}"
        binding.txtInvoiceIdAndDate.text = "$invoiceIdStr • ${item.invoiceDate ?: ""}"

        // Amount
        binding.txtAmount.text = "$${String.format("%.2f", item.invoiceTotal ?: 0f)}"

        // Due Date
        binding.txtDueDate.text = "Due: ${item.invoiceDueDate ?: "N/A"}"

        // Status Badge
        when (item.invoiceStatus) {
            InvoiceStatusEnum.PAID.status -> {
                binding.txtStatusBadge.text = context.getString(R.string.status_paid)
                binding.txtStatusBadge.backgroundTintList = context.getColorStateList(R.color.color_stat_green_bg)
                binding.txtStatusBadge.setTextColor(context.getColor(R.color.color_success))
                binding.avatarContainer.setCardBackgroundColor(context.getColorStateList(R.color.color_stat_green_bg))
                binding.txtInitials.setTextColor(context.getColor(R.color.color_success))
            }
            InvoiceStatusEnum.UN_PAID.status -> {
                binding.txtStatusBadge.text = context.getString(R.string.status_unpaid)
                binding.txtStatusBadge.backgroundTintList = context.getColorStateList(R.color.color_stat_orange_bg)
                binding.txtStatusBadge.setTextColor(context.getColor(R.color.color_stat_orange))
                binding.avatarContainer.setCardBackgroundColor(context.getColorStateList(R.color.color_stat_orange_bg))
                binding.txtInitials.setTextColor(context.getColor(R.color.color_stat_orange))
            }
            else -> {
                binding.txtStatusBadge.text = item.invoiceStatus
                binding.txtStatusBadge.backgroundTintList = context.getColorStateList(R.color.color_neutral_surface_light)
                binding.txtStatusBadge.setTextColor(context.getColor(R.color.color_text_secondary))
            }
        }

        binding.root.setSafeOnClickListener {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.loadViewModelData(item)
                val action = InvoiceMainFragmentDirections.actionFragmentInvoiceMainToEditMain(DBEnum.OLD.entryType)
                navController.navigate(action)
            }
        }

        binding.imgMore.setSafeOnClickListener {
            showRowMenu(binding.imgMore, item)
        }
    }

    private fun showRowMenu(anchor: android.view.View, item: Invoice) {
        val context = anchor.context
        PopupMenu(context, anchor).apply {
            menuInflater.inflate(R.menu.popup_men, menu)
            menu.findItem(R.id.menuItemShare)?.isVisible = false
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuItemDelete -> {
                        BaseDialog.confirm(
                            context = context,
                            title = context.getString(R.string.delete_confirm_title),
                            message = context.getString(R.string.delete_invoice_confirm_message),
                            positiveText = context.getString(R.string.delete_confirm_positive),
                            negativeText = context.getString(R.string.delete_confirm_negative),
                            onConfirm = { viewModel.deleteInvoiceById(item.invoiceId) },
                            onCancel = {}
                        )
                    }
                }
                true
            }
        }.show()
    }

    class InvoiceDiffCallback : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem.invoiceId == newItem.invoiceId

        override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem.copy(invoiceSignature = null) == newItem.copy(invoiceSignature = null)
    }
}
