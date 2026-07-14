package com.koinvois.generator.ui.invoices.add_invoice

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.database.models.Invoice
import com.koinvois.generator.databinding.AddInvoiceMainFragmentBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.ViewPagerAdapterEditInvoice
import com.koinvois.generator.utilities.extensions.reduceDragSensitivity
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.google.android.material.snackbar.Snackbar
import com.webviewtopdf.PdfView
import com.koinvois.generator.utilities.manager.DialogManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AddInvoiceMainActivity : BaseActivity<AddInvoiceMainFragmentBinding>() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private val viewModel: InvoiceMainViewModel by viewModels()

    private val invoiceSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e("sender", result.data?.action.toString())
        }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.root.findViewById<WebView>(R.id.webView)?.let { webView ->
                    testPDFJob(webView, this)
                }
            }
        }

    override fun inflateBinding(): AddInvoiceMainFragmentBinding =
        AddInvoiceMainFragmentBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpViewPagerEditInvoice()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }

        val invoiceType = intent.getStringExtra(EXTRA_INVOICE_TYPE) ?: "new"
        val invoiceId = intent.getIntExtra(EXTRA_INVOICE_ID, -1)

        if (viewModel.invoicePrimaryId == null) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (invoiceType == "new") {
                    viewModel.prepareNewInvoice()
                } else if (invoiceId != -1) {
                    viewModel.loadInvoiceById(invoiceId)
                }
            }
        }
    }

    fun shareInvoice() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            binding.root.findViewById<WebView>(R.id.webView)?.let { webView ->
                testPDFJob(webView, this)
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showSnackBar(this)
        } else {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    fun deleteInvoice() {
        BaseDialog.confirm(
            context = this,
            title = getString(R.string.delete_confirm_title),
            message = "Are you sure you want to delete this Invoice?",
            positiveText = getString(R.string.delete_confirm_positive),
            negativeText = getString(R.string.delete_confirm_negative),
            onConfirm = {
                lifecycleScope.launch(Dispatchers.Default)
                {
                    viewModel.deleteInvoice()
                    withContext(Dispatchers.Main)
                    {
                        finish()
                    }
                }
            }
        )
    }

    fun showPopupMenu(view: View) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.popup_men, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuItemDelete -> {
                        deleteInvoice()
                    }
                    R.id.menuItemShare -> {
                        shareInvoice()
                    }
                }
                true
            }
        }.show()
    }

    private fun saveOnBack() {
        lifecycleScope.launch(Dispatchers.Default) {
            with(viewModel) {
                updateInvoice(
                    Invoice(
                        invoicePrimaryId?.toInt() ?: 0,
                        invoiceNumber,
                        invoiceDate,
                        invoiceTerms,
                        invoiceDueDate,
                        invoicePoNumber,
                        invoiceSubTotal,
                        discountType,
                        discountAmount,
                        discountTotalAmount,
                        taxType,
                        taxLabel,
                        taxRate,
                        taxInclusive,
                        invoiceTotal,
                        invoicePaymentInstructions,
                        signatureObj?.signatureBitmap,
                        signatureObj?.signatureDate,
                        invoiceNotes,
                        invoiceStatus,
                        selectedClient?.clientId,
                        selectedClient?.clientName,
                        selectedClient?.clientEmail,
                        selectedClient?.clientMobile,
                        selectedClient?.clientPhone,
                        selectedClient?.clientFax,
                        selectedClient?.clientContact,
                        selectedClient?.clientAddress1,
                        selectedClient?.clientAddress2,
                        selectedClient?.clientAddress3,
                    )
                )
                appPreferences.setInvoiceNumber(invoiceNumber ?: 0)
                clearViewModel()
            }
            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

    private fun setUpViewPagerEditInvoice() {
        val viewPagerAdapter = ViewPagerAdapterEditInvoice(this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.reduceDragSensitivity()
    }

    private fun testPDFJob(webView: WebView, activity: Activity) {
        val directory: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Invoices/")
        val waitDialog: Dialog = DialogManager.makeWaitDialog(activity)
        waitDialog.show()

        val directoryPath = directory.absolutePath
        PdfView.createWebPrintJob(
            activity,
            webView,
            directoryPath,
            object : PdfView.Callback {
                override fun success(path: String?) {
                    waitDialog.dismiss()
                    path?.let { fileChooser(activity, it) }
                }

                override fun failure(p0: Int) {
                    waitDialog.dismiss()
                }
            })
    }

    private fun fileChooser(activity: Activity, path: String) {
        val file = File(path)
        val uri = FileProvider.getUriForFile(activity, "com.koinvois.generator.provider", file)

        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "application/pdf"
        share.putExtra(Intent.EXTRA_STREAM, uri)

        val chooser = Intent.createChooser(share, "Share File")
        val resInfoList: List<ResolveInfo> = activity.packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            activity.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        invoiceSenderLauncher.launch(chooser)
    }

    private fun showSnackBar(context: Context) {
        val mySnackbar = Snackbar.make(
            binding.root,
            "Open Settings and allow storage permission to continue !",
            Snackbar.LENGTH_LONG
        ).setAction("Open Setting") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        mySnackbar.setActionTextColor(ContextCompat.getColor(context, R.color.primary_color))
        mySnackbar.show()
    }

    companion object {
        private const val EXTRA_INVOICE_TYPE = "extra_invoice_type"
        private const val EXTRA_INVOICE_ID = "extra_invoice_id"

        fun newIntent(context: Context, invoiceType: String, invoiceId: Int = -1): Intent =
            Intent(context, AddInvoiceMainActivity::class.java).apply {
                putExtra(EXTRA_INVOICE_TYPE, invoiceType)
                putExtra(EXTRA_INVOICE_ID, invoiceId)
            }
    }
}
