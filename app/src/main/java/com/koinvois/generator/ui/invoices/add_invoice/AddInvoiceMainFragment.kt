package com.koinvois.generator.ui.invoices.add_invoice

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
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
class AddInvoiceMainFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    var binding: AddInvoiceMainFragmentBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    private val invoiceSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e("sender", result.data?.action.toString())
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddInvoiceMainFragmentBinding.inflate(inflater, container, false)
        setUpViewPagerEditInvoice()
        activity?.let {
            backPressed()
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val invoiceType = arguments?.getString("invoiceType") ?: "new"
        val invoiceId = arguments?.getInt("invoiceId", -1) ?: -1

        if (viewModel.invoicePrimaryId == null) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                if (invoiceType == "new") {
                    viewModel.prepareNewInvoice()
                } else if (invoiceId != -1) {
                    viewModel.loadInvoiceById(invoiceId)
                }
            }
        }
    }

    fun shareInvoice() {
        val context = activity ?: return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            binding?.root?.findViewById<WebView>(R.id.webView)?.let { webView ->
                testPDFJob(webView, context)
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showSnackBar(context)
        } else {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    fun deleteInvoice() {
        activity?.let {
            BaseDialog.confirm(
                context = it,
                title = getString(R.string.delete_confirm_title),
                message = "Are you sure you want to delete this Invoice?",
                positiveText = getString(R.string.delete_confirm_positive),
                negativeText = getString(R.string.delete_confirm_negative),
                onConfirm = {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default)
                    {
                        viewModel.deleteInvoice()
                        withContext(Dispatchers.Main)
                        {
                            findNavController().navigateUp()
                        }
                    }
                }
            )
        }
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

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
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
                findNavController().navigateUp()
            }
        }
    }

    private fun setUpViewPagerEditInvoice() {
        val viewPagerAdapter = ViewPagerAdapterEditInvoice(this)
        binding?.viewPager?.adapter = viewPagerAdapter
        binding?.viewPager?.offscreenPageLimit = 1
        binding?.viewPager?.reduceDragSensitivity()
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding?.root?.findViewById<WebView>(R.id.webView)?.let { webView ->
                    activity?.let { activity ->
                        testPDFJob(webView, activity)
                    }
                }
            }
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
        val mySnackbar = binding?.root?.let { view ->
            Snackbar.make(
                view,
                "Open Settings and allow storage permission to continue !",
                Snackbar.LENGTH_LONG
            ).setAction("Open Setting") {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
        mySnackbar?.setActionTextColor(ContextCompat.getColor(context, R.color.primary_color))
        mySnackbar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
