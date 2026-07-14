package com.koinvois.generator.core.common.dialog

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.koinvois.generator.R

/**
 * Centralized MaterialAlertDialog builder for the recurring confirmation
 * dialogs (delete / logout / backup / restore) instead of each screen
 * hand-rolling its own AlertDialog.Builder.
 */
object BaseDialog {

    fun confirm(
        context: Context,
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_Dialog)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                onCancel?.invoke()
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }
}
