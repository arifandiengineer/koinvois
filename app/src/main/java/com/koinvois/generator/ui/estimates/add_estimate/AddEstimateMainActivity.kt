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
import com.koinvois.generator.databinding.FragmentAddEstimateMainBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.adapter.ViewPagerAdapterEditEstimate
import com.koinvois.generator.utilities.extensions.reduceDragSensitivity
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.webviewtopdf.PdfView
import com.koinvois.generator.utilities.manager.DialogManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AddEstimateMainActivity : BaseActivity<FragmentAddEstimateMainBinding>() {

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

    override fun inflateBinding(): FragmentAddEstimateMainBinding =
        FragmentAddEstimateMainBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setUpViewPagerEditEstimate()
        setClickListeners(this)
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }
    }

    private fun setClickListeners(context: Context) {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.btnShareEstimate.setSafeOnClickListener {
            shareEstimate(context)
        }

        binding.customToolbar.imgSecondaryAction.setSafeOnClickListener {
            showPopupMenu(it, context)
        }
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

    private fun setUpViewPagerEditEstimate() {
        val viewPagerAdapter = ViewPagerAdapterEditEstimate(this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.visible()
        binding.tabLayout.visible()
        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Edit"
                1 -> "Preview"
                else -> "Edit"
            }
        }.attach()
        binding.viewPager.reduceDragSensitivity()
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
            binding.btnShareEstimate,
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
        fun newIntent(context: Context): Intent = Intent(context, AddEstimateMainActivity::class.java)
    }
}
