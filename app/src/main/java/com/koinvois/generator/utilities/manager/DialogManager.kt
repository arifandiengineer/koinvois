package com.koinvois.generator.utilities.manager

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.koinvois.generator.R
import com.koinvois.generator.utilities.extensions.getScreenWidth
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener


object DialogManager {

    fun makeWaitDialog(context: Context): Dialog {

        val waitDialog = Dialog(context)
        waitDialog.setCancelable(false)
        waitDialog.setContentView(R.layout.dialog_wait_custom)
        context.let {
            waitDialog.window?.setLayout(
                it.getScreenWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        return waitDialog
    }

    fun makeDatePickerDialog(context: Context, date: (String) -> Unit): DatePickerDialog {
        val datePickerDialog = DatePickerDialog(context)
        datePickerDialog.setCancelable(false)
        context.let {
            datePickerDialog.window?.setLayout(
                it.getScreenWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }


        datePickerDialog.setOnDateSetListener() { dp, y, m, d ->
            date("$y/${m.plus(1)}/$d")
            Log.e("date", "$y/$m/$d")
        }

        return datePickerDialog
    }

    fun makeDeleteConfirmationDialog(
        msg: String,
        context: Context,
        delete: () -> Unit,
        cancel: () -> Unit
    ): Dialog {
        val deleteConfirmationDialog = Dialog(context)
        deleteConfirmationDialog.setCancelable(false)
        deleteConfirmationDialog.setContentView(R.layout.dialog_delete_confirm)
        context.let {
            deleteConfirmationDialog.window?.setLayout(
                it.getScreenWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        deleteConfirmationDialog.findViewById<TextView>(R.id.txtSuretyDeleteMsg).text = msg

        deleteConfirmationDialog.findViewById<TextView>(R.id.txtCancelDeleteOperation)
            .setSafeOnClickListener {
                cancel()
                deleteConfirmationDialog.dismiss()
            }

        deleteConfirmationDialog.findViewById<TextView>(R.id.txtOkaDeleteOperation)
            .setSafeOnClickListener {
                delete()
                deleteConfirmationDialog.dismiss()
            }

        return deleteConfirmationDialog
    }

//    fun getOverLayPermissionDialog(
//        context: Context,
//        onCancelCLick: () -> Unit,
//        onPermitClick: () -> Unit
//    ): Dialog {
//        val overlayPermissionDialog = Dialog(context)
//        overlayPermissionDialog.setCancelable(false)
//        overlayPermissionDialog.setContentView(R.layout.overlay_permission_dialog)
//        context.let { overlayPermissionDialog.window?.setLayout(it.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT) }
//
//        val btnCancel = overlayPermissionDialog.findViewById<MaterialButton>(R.id.btnCancelOverLay)
//        val btnPermit = overlayPermissionDialog.findViewById<MaterialButton>(R.id.btnPermitOverlay)
//        btnCancel.setSafeOnClickListener {
//            onCancelCLick()
//
//            overlayPermissionDialog.dismiss()
//        }
//
//        btnPermit.setSafeOnClickListener {
//            onPermitClick()
//
//            overlayPermissionDialog.dismiss()
//        }
//
//        return overlayPermissionDialog
//    }

//    fun getUsageAccessPermissionDialog(
//        context: Context,
//        onCancelCLick: () -> Unit,
//        onPermitClick: () -> Unit
//    ): Dialog {
//        val usageAccessPermissionDialog = Dialog(context)
//        usageAccessPermissionDialog.setCancelable(false)
//        usageAccessPermissionDialog.setContentView(R.layout.usage_access_permission_dialog)
//        context.let { usageAccessPermissionDialog.window?.setLayout(it.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT) }
//
//        val btnCancel = usageAccessPermissionDialog.findViewById<MaterialButton>(R.id.btnCancelUsageAccess)
//        val btnPermit = usageAccessPermissionDialog.findViewById<MaterialButton>(R.id.btnPermitUsageAccess)
//        btnCancel.setSafeOnClickListener {
//            onCancelCLick()
//
//            usageAccessPermissionDialog.dismiss()
//        }
//
//        btnPermit.setSafeOnClickListener {
//            onPermitClick()
//
//            usageAccessPermissionDialog.dismiss()
//        }
//
//        return usageAccessPermissionDialog
//
//    }


//    fun getDeleteConfirmationDialog(context: Context): Dialog {
//        val deleteConfirmationDialog = Dialog(context)
//        deleteConfirmationDialog.setContentView(R.layout.delete_confirmation_dialog)
//        deleteConfirmationDialog.setCancelable(false)
//        deleteConfirmationDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        context.getScreenWidth().let { it1 -> deleteConfirmationDialog.window?.setLayout(it1, ViewGroup.LayoutParams.WRAP_CONTENT) }
//
//        return deleteConfirmationDialog
//    }
//
//    fun getCustomDeletingDialog(context: Context): Dialog {
//
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.deleting_custom_dialog)
//        dialog.setCancelable(false)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        context.getScreenWidth().let { it1 -> dialog.window?.setLayout(it1, ViewGroup.LayoutParams.WRAP_CONTENT) }
//
//        return dialog
//    }
//
//    fun getAfterDeleteDialog(context: Context): Dialog {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.after_delete_dialog)
//        dialog.setCancelable(false)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        context.getScreenWidth().let { it1 -> dialog.window?.setLayout(it1, ViewGroup.LayoutParams.WRAP_CONTENT) }
//
//        return dialog
//    }
}