package com.koinvois.generator.ui.invoices.add_invoice

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.core.utils.CurrencyFormatter
import com.koinvois.generator.database.models.Invoice
import com.koinvois.generator.databinding.ActivityInvoiceEditBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ClientDetailForInvoiceActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ClientListForInvoiceActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.DiscountActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.AddPhotoToInvoiceActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.EditBusinessDetailsFromInvoiceActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.InvoiceInformationActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.InvoicePaymentsListActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ItemDetailForInvoiceActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.SignatureActivity
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.TaxActivity
import com.koinvois.generator.ui.invoices.add_invoice.sub_fragments.PreviewInvoiceFragment
import com.koinvois.generator.ui.invoices.adapter.SelectedInvoiceItemsAdapter
import com.koinvois.generator.ui.invoices.adapter.SelectedPhotosForInvoiceAdapter
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.utilities.extensions.*
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
class AddInvoiceMainActivity : BaseActivity<ActivityInvoiceEditBinding>() {

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

    override fun inflateBinding(): ActivityInvoiceEditBinding =
        ActivityInvoiceEditBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners()
        observePaymentSummary()

        supportFragmentManager.beginTransaction()
            .replace(R.id.previewContainer, PreviewInvoiceFragment())
            .commit()

        onBackPressedDispatcher.addCallback(this) {
            if (binding.previewContainer.visibility == View.VISIBLE) {
                hidePreview()
            } else {
                saveOnBack()
            }
        }

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

    override fun onResume() {
        super.onResume()

        viewModel.selectedClient.let {
            binding.txtClientName.text = it?.clientName
        }

        viewModel.signatureObj?.let {
            // binding.txtSignature.text = "Signed on ${it?.signatureDate}"
        }
        viewModel.recalculateInvoiceSubTotal()
        viewModel.recalculateInvoiceTotal()

        binding.txtSubtotal.text = CurrencyFormatter.format(viewModel.invoiceSubTotal ?: 0f)
        binding.txtDiscountValue.text = "-" + CurrencyFormatter.format(viewModel.discountTotalAmount ?: 0f)
        binding.txtTaxLabel.text = if (viewModel.taxRate != null) {
            "Tax (${viewModel.taxRate?.let { rate -> if (rate == rate.toInt().toFloat()) rate.toInt().toString() else rate.toString() }}%)"
        } else {
            "Tax"
        }
        binding.txtTaxValue.text = CurrencyFormatter.format(viewModel.taxAmount)
        binding.txtTotalAmount.text = CurrencyFormatter.format(viewModel.invoiceTotal)

        viewModel.invoiceNumber?.let {
            binding.txtInvoiceNumber.text = "INV-$it"
        } ?: run {
            binding.txtInvoiceNumber.text = null
        }

        viewModel.invoiceDate?.let {
            binding.txtInvoiceDate.text = it
        }

        viewModel.selectedItemsList?.let {
            binding.rvInvoiceItems.adapter =
                SelectedInvoiceItemsAdapter(it, viewModel) {
                    startActivity(ItemDetailForInvoiceActivity.newIntent(this, DBEnum.OLD.entryType))
                }
        }

        viewModel.photosForInvoice?.let {
            binding.rvPhotos.adapter =
                SelectedPhotosForInvoiceAdapter(it, viewModel) {
                    startActivity(AddPhotoToInvoiceActivity.newIntent(this, DBEnum.OLD.entryType))
                }
        }

        viewModel.invoicePaymentInstructions?.let {
            binding.txtPaymentInstruction.setText(it)
        }

        viewModel.invoiceNotes?.let {
            binding.txtNotes.setText(it)
        }

        viewModel.invoiceStatus?.let {
            when (it) {
                InvoiceStatusEnum.UN_PAID.status -> {
                    binding.btnMarkPaid.setText(R.string.label_mark_paid)
                }
                InvoiceStatusEnum.PAID.status -> {
                    binding.btnMarkPaid.setText(R.string.label_mark_unpaid)
                }
                else -> {
                    binding.btnMarkPaid.setText(R.string.label_mark_paid)
                }
            }
        }
    }

    private fun observePaymentSummary() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.totalPaidAmount.collect { total ->
                        binding.txtPaymentValue.text = CurrencyFormatter.format(total)
                    }
                }
                launch {
                    viewModel.balanceDue.collect { balance ->
                        binding.txtTotalBalanceAmount.text = CurrencyFormatter.format(balance)
                    }
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.apply {
            btnBack.visible()
            txtToolbarTitle.text = getString(R.string.label_edit_invoice)

            // Three-dot menu for Share/Delete
            imgSecondaryAction.visible()
            imgSecondaryAction.setImageResource(R.drawable.icon_three_dot)
            imgSecondaryAction.setSafeOnClickListener {
                showPopupMenu(it)
            }

            // Forward button now shows the Preview overlay in place of the old ViewPager tab
            imgRightAction.visible()
            imgRightAction.setImageResource(R.drawable.btn_forward)
            imgRightAction.setColorFilter(getColor(R.color.yellow_tab_indicator))
            imgRightAction.setSafeOnClickListener {
                showPreview()
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setClickListeners() {

        binding.txtTaxPrice.setSafeOnClickListener {
            startActivity(TaxActivity.newIntent(this))
        }

        binding.txtSignature.setSafeOnClickListener {
            startActivity(SignatureActivity.newIntent(this))
        }

        binding.txtAddPhoto.setSafeOnClickListener {
            startActivity(AddPhotoToInvoiceActivity.newIntent(this, DBEnum.NEW.entryType))
        }

        binding.btnInvoiceDate.setSafeOnClickListener {
            startActivity(InvoiceInformationActivity.newIntent(this))
        }

        binding.btnInvoiceNumber.setSafeOnClickListener {
            startActivity(InvoiceInformationActivity.newIntent(this))
        }

        binding.btnDueOnReceipt.setSafeOnClickListener {
            startActivity(InvoiceInformationActivity.newIntent(this))
        }

        binding.btnBusinessInfo.setSafeOnClickListener {
            startActivity(EditBusinessDetailsFromInvoiceActivity.newIntent(this))
        }

        binding.secondCard.setSafeOnClickListener {
            viewModel.selectedClient?.let {
                startActivity(ClientDetailForInvoiceActivity.newIntent(this))
            } ?: run {
                when (viewModel.allClients?.isNotEmpty()) {
                    true -> {
                        startActivity(ClientListForInvoiceActivity.newIntent(this))
                    }
                    false -> {
                        startActivity(ClientDetailForInvoiceActivity.newIntent(this))
                    }
                    null -> {
                        binding.root.showErrorSnackbar(getString(R.string.error_try_again))
                    }
                }
            }
        }

        binding.txtAddItem.setSafeOnClickListener {
            startActivity(ItemDetailForInvoiceActivity.newIntent(this, DBEnum.NEW.entryType))
        }

        binding.txtDiscountPrice.setSafeOnClickListener {
            startActivity(DiscountActivity.newIntent(this))
        }

        binding.btnMarkPaid.setSafeOnClickListener {
            when (viewModel.invoiceStatus) {
                InvoiceStatusEnum.PAID.status -> {
                    viewModel.invoiceStatus = InvoiceStatusEnum.UN_PAID.status
                    binding.btnMarkPaid.setText(R.string.label_mark_paid)
                }
                InvoiceStatusEnum.UN_PAID.status -> {
                    viewModel.invoiceStatus = InvoiceStatusEnum.PAID.status
                    binding.btnMarkPaid.setText(R.string.label_mark_unpaid)
                }
                else -> {
                    viewModel.invoiceStatus = InvoiceStatusEnum.PAID.status
                    binding.btnMarkPaid.setText(R.string.label_mark_unpaid)
                }
            }
        }

        binding.txtPaymentPrice.setSafeOnClickListener {
            startActivity(InvoicePaymentsListActivity.newIntent(this))
        }

        binding.txtNotes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val notes = binding.txtNotes.text.toString()
                viewModel.invoiceNotes = notes
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.txtPaymentInstruction.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val instructions = binding.txtPaymentInstruction.text.toString()
                viewModel.invoicePaymentInstructions = instructions
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun showPreview() {
        binding.previewContainer.visible()
    }

    private fun hidePreview() {
        binding.previewContainer.hide()
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
