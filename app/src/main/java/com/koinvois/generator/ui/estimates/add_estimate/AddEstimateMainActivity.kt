package com.koinvois.generator.ui.estimates.add_estimate

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.TextView
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
import com.koinvois.generator.database.models.Estimate
import com.koinvois.generator.databinding.ActivityEstimateEditBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.PreviewEstimateFragment
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.AddPhotoToEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ClientDetailForEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ClientListForEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EditBusinessDetailsFromEstimateActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateDiscountActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateInformationActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateSignatureActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.EstimateTaxActivity
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ItemDetailForEstimateActivity
import com.koinvois.generator.ui.estimates.adapter.SelectedEstimateItemsAdapter
import com.koinvois.generator.ui.estimates.adapter.SelectedPhotosForEstimateAdapter
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.showErrorSnackbar
import com.koinvois.generator.utilities.extensions.visible
import com.koinvois.generator.utilities.extensions.hide
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
class AddEstimateMainActivity : BaseActivity<ActivityEstimateEditBinding>() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private val viewModel: EstimatesMainViewModel by viewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.root.findViewById<WebView>(R.id.webView)?.let { webView ->
                    testPDFJob(webView, this)
                }
            }
        }

    override fun inflateBinding(): ActivityEstimateEditBinding =
        ActivityEstimateEditBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpToolbar()
        setClickListeners(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.previewContainer, PreviewEstimateFragment())
            .commit()

        onBackPressedDispatcher.addCallback(this) {
            if (binding.previewContainer.visibility == View.VISIBLE) {
                hidePreview()
            } else {
                saveOnBack()
            }
        }

        val estimateType = intent.getStringExtra(EXTRA_ESTIMATE_TYPE) ?: "new"
        val estimateId = intent.getIntExtra(EXTRA_ESTIMATE_ID, -1)

        if (viewModel.estimatePrimaryId == null) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (estimateType == "new") {
                    viewModel.prepareNewEstimate()
                } else if (estimateId != -1) {
                    viewModel.loadEstimateById(estimateId)
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
            binding.txtSignature.text = "Signed on ${it?.signatureDate}"
        }
        var totalItemsCost = 0f

        viewModel.selectedItemsList?.forEach {
            it.toString().let { it1 -> Log.e("obj", it1) }
            totalItemsCost += it.itemTotal ?: 0f
        }
        binding.txtTotalAmount.text = totalItemsCost.toString()

        viewModel.estimateNumber?.let {
            binding.txtEstimateNumber.text = it.toString()
        } ?: run {
            binding.txtEstimateNumber.text = null
        }

        viewModel.estimateDate?.let {
            binding.txtEstimateDate.text = it
        }

        viewModel.selectedItemsList?.let {
            binding.rvEstimateItems.adapter =
                SelectedEstimateItemsAdapter(it, viewModel) {
                    startActivity(ItemDetailForEstimateActivity.newIntent(this, DBEnum.OLD.entryType))
                }
        }

        viewModel.photosForEstimate?.let {
            binding.rvPhotos.adapter =
                SelectedPhotosForEstimateAdapter(it, viewModel) {
                    startActivity(AddPhotoToEstimateActivity.newIntent(this, DBEnum.OLD.entryType))
                }
        }

        viewModel.discountAmount?.let {
            binding.txtDiscountPrice.text = it.toString()
        }

        viewModel.estimateNotes?.let {
            binding.txtNotes.setText(it)
        }

        viewModel.estimateStatus?.let {
            when (it) {
                EstimateStatusEnum.OPEN.status -> {
                    binding.btnMarkPaid.setText(R.string.label_mark_closed)
                }
                EstimateStatusEnum.CLOSED.status -> {
                    binding.btnMarkPaid.setText(R.string.label_mark_open)
                }
                else -> {
                    binding.btnMarkPaid.setText(R.string.label_mark_closed)
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding.customToolbar.apply {
            imgRightAction.visible()
            imgRightAction.setImageResource(R.drawable.btn_forward)
            imgRightAction.setColorFilter(getColor(R.color.yellow_tab_indicator))
            imgRightAction.setSafeOnClickListener {
                showPreview()
            }
        }
    }

    private fun setClickListeners(context: Context) {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.customToolbar.imgSecondaryAction.setSafeOnClickListener {
            showPopupMenu(it, context)
        }

        binding.txtSignature.setSafeOnClickListener {
            startActivity(EstimateSignatureActivity.newIntent(this))
        }

        binding.txtEstimateDate.setSafeOnClickListener {
            startActivity(EstimateInformationActivity.newIntent(this))
        }

        binding.txtEstimateNumber.setSafeOnClickListener {
            startActivity(EstimateInformationActivity.newIntent(this))
        }

        binding.txtBusinessInfo.setSafeOnClickListener {
            startActivity(EditBusinessDetailsFromEstimateActivity.newIntent(this))
        }

        binding.cardClient.setSafeOnClickListener {
            viewModel.selectedClient?.let {
                startActivity(ClientDetailForEstimateActivity.newIntent(this))
            } ?: run {
                when (viewModel.allClients?.isNotEmpty()) {
                    true -> {
                        startActivity(ClientListForEstimateActivity.newIntent(this))
                    }
                    false -> {
                        startActivity(ClientDetailForEstimateActivity.newIntent(this))
                    }
                    null -> {
                        binding.root.showErrorSnackbar(getString(R.string.error_try_again))
                    }
                }
            }
        }

        binding.txtAddItem.setSafeOnClickListener {
            startActivity(ItemDetailForEstimateActivity.newIntent(this, DBEnum.NEW.entryType))
        }

        binding.txtDiscountPrice.setSafeOnClickListener {
            startActivity(EstimateDiscountActivity.newIntent(this))
        }

        binding.txtTaxPrice.setSafeOnClickListener {
            startActivity(EstimateTaxActivity.newIntent(this))
        }

        binding.txtAddPhoto.setSafeOnClickListener {
            startActivity(AddPhotoToEstimateActivity.newIntent(this, DBEnum.NEW.entryType))
        }

        binding.btnMarkPaid.setSafeOnClickListener {
            when (viewModel.estimateStatus) {
                EstimateStatusEnum.OPEN.status -> {
                    viewModel.estimateStatus = EstimateStatusEnum.CLOSED.status
                    binding.btnMarkPaid.setText(R.string.label_mark_open)
                }
                EstimateStatusEnum.CLOSED.status -> {
                    viewModel.estimateStatus = EstimateStatusEnum.OPEN.status
                    binding.btnMarkPaid.setText(R.string.label_mark_closed)
                }
                else -> {
                    viewModel.estimateStatus = EstimateStatusEnum.CLOSED.status
                    binding.btnMarkPaid.setText(R.string.label_mark_open)
                }
            }
        }

        binding.txtNotes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val notes = binding.txtNotes.text.toString()
                viewModel.estimateNotes = notes
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

    private fun shareEstimate(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            binding.root.findViewById<WebView>(R.id.webView)?.let { webView ->
                testPDFJob(webView, this)
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showSnackBar(context)
        } else {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun showPopupMenu(view: View, context: Context) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.popup_men, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuItemDelete -> {
                        deleteEstimate()
                    }
                    R.id.menuItemShare -> {
                        shareEstimate(context)
                    }
                }
                true
            }
        }.show()
    }

    private fun deleteEstimate() {
        BaseDialog.confirm(
            context = this,
            title = getString(R.string.delete_confirm_title),
            message = "Are you sure you want to delete this Estimate?",
            positiveText = getString(R.string.delete_confirm_positive),
            negativeText = getString(R.string.delete_confirm_negative),
            onConfirm = {
                lifecycleScope.launch(Dispatchers.Default)
                {
                    viewModel.deleteEstimate()
                    withContext(Dispatchers.Main)
                    {
                        finish()
                    }
                }
            }
        )
    }

    private fun saveOnBack() {
        lifecycleScope.launch(Dispatchers.Default) {
            with(viewModel) {
                updateEstimate(
                    Estimate(
                        estimatePrimaryId?.toInt() ?: 0,
                        estimateNumber,
                        estimateDate,
                        estimatePoNumber = estimatePoNumber,
                        estimateSubtotal = estimateSubTotal,
                        estimateDiscountType = discountType,
                        estimateDiscountAmount = discountAmount,
                        discountTotalAmount = discountTotalAmount,
                        estimateTaxType = taxType,
                        estimateTaxLabel = taxLabel,
                        estimateTaxRate = taxRate,
                        estimateTaxInclusive = taxInclusive,
                        estimateTotal = estimateTotal,
                        estimateSignature = signatureObj?.signatureBitmap,
                        signatureDate = signatureObj?.signatureDate,
                        estimateNotes = estimateNotes,
                        estimateStatus = estimateStatus,
                        clientPK = selectedClient?.clientId,
                        estimateClientName = selectedClient?.clientName,
                        estimateClientEmail = selectedClient?.clientEmail,
                        estimateClientMobile = selectedClient?.clientMobile,
                        estimateClientPhone = selectedClient?.clientPhone,
                        estimateClientFax = selectedClient?.clientFax,
                        estimateClientContact = selectedClient?.clientContact,
                        estimateClientAddress1 = selectedClient?.clientAddress1,
                        estimateClientAddress2 = selectedClient?.clientAddress2,
                        estimateClientAddress3 = selectedClient?.clientAddress3,
                    )
                )
                appPreferences.setEstimateNumber(estimateNumber ?: 0)
                clearViewModel()
            }
            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

    private fun testPDFJob(webView: WebView, actv: Activity) {
        val directory: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Estimates/")
        val waitDialog: Dialog = DialogManager.makeWaitDialog(actv)
        waitDialog.show()

        val directoryPath = directory.absolutePath
        PdfView.createWebPrintJob(
            actv,
            webView,
            directoryPath,
            object : PdfView.Callback {
                override fun success(path: String?) {
                    waitDialog.dismiss()
                    path?.let { fileChooser(actv, it) }
                }

                override fun failure(p0: Int) {
                    waitDialog.dismiss()
                }
            })
    }

    private fun fileChooser(actv: Activity, path: String) {
        val file = File(path)
        val uri = FileProvider.getUriForFile(actv, "com.koinvois.generator.provider", file)

        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "application/pdf"
        share.putExtra(Intent.EXTRA_STREAM, uri)

        val chooser = Intent.createChooser(share, "Share File")
        val resInfoList: List<ResolveInfo> = actv.packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            actv.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        startActivity(chooser)
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

        val snackbarView = mySnackbar.view
        snackbarView.setBackgroundColor(Color.WHITE)
        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(Color.BLACK)
        textView?.textSize = 18f

        mySnackbar.show()
    }

    companion object {
        private const val EXTRA_ESTIMATE_TYPE = "extra_estimate_type"
        private const val EXTRA_ESTIMATE_ID = "extra_estimate_id"

        fun newIntent(context: Context, estimateType: String, estimateId: Int = -1): Intent =
            Intent(context, AddEstimateMainActivity::class.java).apply {
                putExtra(EXTRA_ESTIMATE_TYPE, estimateType)
                putExtra(EXTRA_ESTIMATE_ID, estimateId)
            }
    }
}
