package com.koinvois.generator.ui.invoices.add_invoice

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Environment
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
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ItemsListForInvoiceActivity
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

    override fun inflateBinding(): ActivityInvoiceEditBinding =
        ActivityInvoiceEditBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        val invoiceType = intent.getStringExtra(EXTRA_INVOICE_TYPE) ?: "new"
        val invoiceId = intent.getIntExtra(EXTRA_INVOICE_ID, -1)

        setUpToolbar(invoiceType)
        setClickListeners()
        observePaymentSummary()

        supportFragmentManager.beginTransaction()
            .replace(R.id.previewContainer, PreviewInvoiceFragment())
            .commit()

        onBackPressedDispatcher.addCallback(this) {
            if (binding.previewContainer.visibility == View.VISIBLE) {
                hidePreview()
            } else {
                showExitConfirmation()
            }
        }

        if (viewModel.invoicePrimaryId == null) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (invoiceType == "new") {
                    viewModel.prepareNewInvoice()
                } else if (invoiceId != -1) {
                    viewModel.loadInvoiceById(invoiceId)
                }
                refreshInvoiceUi()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshInvoiceUi()
    }

    private fun refreshInvoiceUi() {
        Log.d("AutofillLog", "UI Refresh - Number: ${viewModel.invoiceNumber}, Date: ${viewModel.invoiceDate}, Business: ${viewModel.businessUpdateModel?.businessName}")

        viewModel.selectedClient.let {
            binding.txtClientName.text = it?.clientName ?: getString(R.string.fallback_no_client)
        }

        binding.txtBusinessInfo.text = viewModel.businessUpdateModel?.businessName ?: getString(R.string.label_business_info)

        viewModel.signatureObj?.let {
            if (it.signatureBitmap != null) {
                binding.imgSignaturePreview.visible()
                binding.imgSignaturePreview.setImageBitmap(it.signatureBitmap)
                binding.txtTapToAddSignature.hide()
            } else {
                binding.imgSignaturePreview.hide()
                binding.txtTapToAddSignature.visible()
            }
        } ?: run {
            binding.imgSignaturePreview.hide()
            binding.txtTapToAddSignature.visible()
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
            if (it.isNotEmpty()) {
                binding.txtAddPhotoLabel.text = "Update Photo"
                binding.rvPhotos.adapter =
                    SelectedPhotosForInvoiceAdapter(it, viewModel) {
                        startActivity(AddPhotoToInvoiceActivity.newIntent(this, DBEnum.OLD.entryType))
                    }
            } else {
                binding.txtAddPhotoLabel.text = getString(R.string.label_add_photo)
            }
        } ?: run {
            binding.txtAddPhotoLabel.text = getString(R.string.label_add_photo)
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

    private fun setUpToolbar(invoiceType: String) {
        binding.customToolbar.apply {
            btnBack.visible()
            txtToolbarTitle.text = if (invoiceType == "new") {
                getString(R.string.label_add_invoice)
            } else {
                getString(R.string.label_edit_invoice)
            }

            // Three-dot menu only for existing invoice
            if (invoiceType == "new") {
                imgSecondaryAction.hide()
            } else {
                imgSecondaryAction.visible()
                imgSecondaryAction.setImageResource(R.drawable.icon_three_dot)
                imgSecondaryAction.setSafeOnClickListener {
                    showPopupMenu(it)
                }
            }

            // Forward button shows the Preview
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

    private fun showExitConfirmation() {
        BaseDialog.confirm(
            context = this,
            title = "Keluar?",
            message = "Apakah anda yakin ingin meninggalkann halaman ini tanpa menyimpan?",
            positiveText = "Ya, Keluar",
            negativeText = "Batal",
            onConfirm = {
                viewModel.clearViewModel()
                finish()
            }
        )
    }

    private fun setClickListeners() {

        binding.txtTaxPrice.setSafeOnClickListener {
            startActivity(TaxActivity.newIntent(this))
        }

        binding.txtSignature.setSafeOnClickListener {
            startActivity(SignatureActivity.newIntent(this))
        }

        binding.txtAddPhoto.setSafeOnClickListener {
            val photos = viewModel.photosForInvoice
            if (!photos.isNullOrEmpty()) {
                // If photo exists, edit the first one
                viewModel.currentSelectedPhoto = photos[0]
                startActivity(AddPhotoToInvoiceActivity.newIntent(this, DBEnum.OLD.entryType))
            } else {
                startActivity(AddPhotoToInvoiceActivity.newIntent(this, DBEnum.NEW.entryType))
            }
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
            // Always go to Client List so user can choose OR add new from there
            startActivity(ClientListForInvoiceActivity.newIntent(this))
        }

        binding.txtAddItem.setSafeOnClickListener {
            // New logic: if items are empty, go direct to add form. Otherwise go to list.
            if (viewModel.allItems.isNullOrEmpty()) {
                startActivity(ItemDetailForInvoiceActivity.newIntent(this, DBEnum.NEW.entryType))
            } else {
                startActivity(ItemsListForInvoiceActivity.newIntent(this))
            }
        }

        binding.btnSaveInvoice.setSafeOnClickListener {
            saveInvoice()
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
        // Refresh the fragment to ensure it has latest data from ViewModel/Draft
        (supportFragmentManager.findFragmentById(R.id.previewContainer) as? PreviewInvoiceFragment)?.refreshData()
    }

    private fun hidePreview() {
        binding.previewContainer.hide()
    }

    fun shareInvoice() {
        // The generated PDF is written to app-specific external storage (see testPDFJob),
        // which needs no runtime permission on any API level, so WRITE_EXTERNAL_STORAGE is
        // not required here.

        // previewContainer is `visibility="gone"` (0x0, never laid out) until the user opens
        // the preview overlay. Printing a WebView that was never measured/laid out produces a
        // blank PDF, so it must be made visible and given a layout pass - and its HTML must
        // finish loading (onPageFinished) - before the print job is triggered.
        binding.previewContainer.visible()
        val previewFragment =
            supportFragmentManager.findFragmentById(R.id.previewContainer) as? PreviewInvoiceFragment
        previewFragment?.refreshData {
            binding.previewContainer.post {
                binding.root.findViewById<WebView>(R.id.webView)?.let { webView ->
                    testPDFJob(webView, this) {
                        binding.previewContainer.hide()
                    }
                }
            }
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

    private fun saveInvoice() {
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

    private fun saveOnBack() {
        // Method kept but logic moved to saveInvoice() to avoid auto-save on back
    }

    private fun testPDFJob(webView: WebView, activity: Activity, onDone: () -> Unit = {}) {
        // App-specific external storage: no runtime permission needed on any API level,
        // and unaffected by scoped storage (unlike getExternalStoragePublicDirectory, which
        // silently fails to write on API 30+ since requestLegacyExternalStorage is inert
        // once targetSdk >= 30 - see AndroidManifest.xml TODO).
        val directory = File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Invoices")
        directory.mkdirs()
        val waitDialog: Dialog = DialogManager.makeWaitDialog(activity)
        waitDialog.show()

        // PdfView/PdfPrint treats this as the exact output file path, not a directory,
        // so it must include a filename with the .pdf extension.
        val filePath = File(directory, "Invoice_${viewModel.invoiceNumber ?: System.currentTimeMillis()}.pdf").absolutePath
        PdfView.createWebPrintJob(
            activity,
            webView,
            filePath,
            object : PdfView.Callback {
                override fun success(path: String?) {
                    waitDialog.dismiss()
                    path?.let { fileChooser(activity, it) }
                    onDone()
                }

                override fun failure(p0: Int) {
                    waitDialog.dismiss()
                    Snackbar.make(binding.root, "Failed to generate PDF for sharing", Snackbar.LENGTH_LONG).show()
                    onDone()
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
