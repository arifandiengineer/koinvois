package com.koinvois.generator.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import com.koinvois.generator.core.common.bottomsheet.BaseBottomSheet
import com.koinvois.generator.databinding.BottomSheetQuickActionBinding

/**
 * MD3 bottom sheet reference implementation for FAB quick actions on Dashboard.
 * Replaces the plain PopupMenu previously used here — pure navigation, no business logic.
 */
class QuickActionBottomSheet(
    private val onNewInvoice: () -> Unit,
    private val onNewEstimate: () -> Unit
) : BaseBottomSheet<BottomSheetQuickActionBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BottomSheetQuickActionBinding = BottomSheetQuickActionBinding.inflate(inflater, container, false)

    override fun setupView() {
        binding.rowNewInvoice.setOnClickListener {
            onNewInvoice()
            dismiss()
        }
        binding.rowNewEstimate.setOnClickListener {
            onNewEstimate()
            dismiss()
        }
    }
}
