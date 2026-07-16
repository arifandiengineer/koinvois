package com.koinvois.generator.ui.invoices.add_invoice.sub_fragments

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentInvoicePreviewBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.utility

class PreviewInvoiceFragment : Fragment() {

    private var binding: FragmentInvoicePreviewBinding? = null
    private val viewModel: InvoiceMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoicePreviewBinding.inflate(inflater, container, false)
        setupWebView()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshData()
    }

    private fun setupWebView() {
        binding?.webView?.settings?.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            domStorageEnabled = true
        }
    }

    fun refreshData(onLoaded: (() -> Unit)? = null) {
        // Always (re)set the client: a stale one from a previous call would otherwise keep
        // firing its (now-invalid) onLoaded closure on every future page load.
        binding?.webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onLoaded?.invoke()
            }
        }
        createHTML()
    }

    private fun createHTML() {
        val htmlContent = StringBuilder()
        
        // CSS & Head
        htmlContent.append("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { font-family: sans-serif; margin: 0; padding: 15px; color: #333; background: #fff; line-height: 1.4; }
                    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
                    .logo { max-width: 80px; max-height: 80px; object-fit: contain; }
                    .biz-info { text-align: right; }
                    .biz-name { font-size: 18px; font-weight: bold; margin: 0; }
                    .biz-email { font-size: 12px; color: #666; margin: 2px 0; }
                    
                    .invoice-bar { background: #2A44A2; color: #fff; padding: 10px 15px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
                    .invoice-label { font-size: 24px; font-weight: bold; margin: 0; }
                    
                    .details-grid { display: flex; justify-content: space-between; margin-bottom: 20px; }
                    .bill-to { flex: 1; }
                    .inv-meta { text-align: right; }
                    .label { color: #888; font-size: 11px; text-transform: uppercase; margin-bottom: 4px; }
                    .value { font-weight: bold; font-size: 14px; margin-bottom: 10px; }
                    
                    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
                    th { background: #f4f4f4; text-align: left; padding: 10px; font-size: 12px; border-bottom: 2px solid #ddd; }
                    td { padding: 10px; font-size: 13px; border-bottom: 1px solid #eee; }
                    .text-right { text-align: right; }
                    
                    .summary-section { display: flex; justify-content: flex-end; }
                    .summary-table { width: 250px; }
                    .summary-row { display: flex; justify-content: space-between; padding: 5px 0; font-size: 14px; }
                    .total-row { background: #2A44A2; color: #fff; padding: 10px; margin-top: 10px; font-weight: bold; font-size: 18px; }
                    
                    .footer { margin-top: 40px; border-top: 1px solid #eee; padding-top: 20px; }
                    .payment-info { margin-bottom: 20px; font-size: 12px; }
                    .signature-area { text-align: right; }
                    .signature-img { max-width: 150px; border-bottom: 1px solid #ccc; }
                    
                    .photos-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; margin-top: 20px; }
                    .photo-item { border: 1px solid #eee; padding: 5px; }
                    .photo-img { width: 100%; height: auto; display: block; }
                    .photo-desc { font-size: 10px; font-weight: bold; margin-top: 4px; }
                </style>
            </head>
            <body>
        """.trimIndent())

        // Header
        val logoBase64 = utility.encodeToBase64(viewModel.businessUpdateModel?.businessLogo) ?: ""
        htmlContent.append("""
            <div class="header">
                <img class="logo" src="data:image/png;base64,$logoBase64" onerror="this.style.display='none'">
                <div class="biz-info">
                    <p class="biz-name">${viewModel.businessUpdateModel?.businessName ?: ""}</p>
                    <p class="biz-email">${viewModel.businessUpdateModel?.businessEmail ?: ""}</p>
                </div>
            </div>
            
            <div class="invoice-bar">
                <p class="invoice-label">INVOICE</p>
                <span>#${viewModel.invoiceNumber ?: ""}</span>
            </div>
            
            <div class="details-grid">
                <div class="bill-to">
                    <div class="label">Invoice To</div>
                    <div class="value">${viewModel.selectedClient?.clientName ?: ""}</div>
                    <div style="font-size: 12px; color: #555;">
                        ${viewModel.selectedClient?.clientAddress1 ?: ""}<br>
                        ${viewModel.selectedClient?.clientAddress2 ?: ""}<br>
                        ${viewModel.selectedClient?.clientAddress3 ?: ""}
                    </div>
                </div>
                <div class="inv-meta">
                    <div class="label">Date</div>
                    <div class="value">${viewModel.invoiceDate ?: ""}</div>
                    <div class="label">Due Date</div>
                    <div class="value">${viewModel.invoiceDueDate ?: ""}</div>
                </div>
            </div>
        """.trimIndent())

        // Table
        htmlContent.append("""
            <table>
                <thead>
                    <tr>
                        <th>Item</th>
                        <th class="text-right">Price</th>
                        <th class="text-right">Qty</th>
                        <th class="text-right">Total</th>
                    </tr>
                </thead>
                <tbody>
        """.trimIndent())

        viewModel.selectedItemsList?.forEach { item ->
            htmlContent.append("""
                <tr>
                    <td>${item.invoiceItemName}</td>
                    <td class="text-right">${item.invoiceItemUnitCost}</td>
                    <td class="text-right">${item.invoiceItemQuantity}</td>
                    <td class="text-right">${item.itemTotal}</td>
                </tr>
            """.trimIndent())
        }

        htmlContent.append("</tbody></table>")

        // Summary
        htmlContent.append("""
            <div class="summary-section">
                <div class="summary-table">
                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span>${viewModel.invoiceSubTotal ?: 0}</span>
                    </div>
                    <div class="summary-row">
                        <span>Discount</span>
                        <span>-${viewModel.discountTotalAmount ?: 0}</span>
                    </div>
                    <div class="summary-row">
                        <span>Tax</span>
                        <span>${viewModel.taxAmount ?: 0}</span>
                    </div>
                    <div class="summary-row total-row">
                        <span>Total</span>
                        <span>${viewModel.invoiceTotal ?: 0}</span>
                    </div>
                </div>
            </div>
        """.trimIndent())

        // Payment Info & Signature
        htmlContent.append("""
            <div class="footer">
                <div class="payment-info">
                    <div class="label">Payment Instructions</div>
                    <div>${viewModel.invoicePaymentInstructions?.replace("\n", "<br>") ?: "No instructions provided."}</div>
                </div>
                
                <div style="display: flex; justify-content: space-between; align-items: flex-end;">
                    <div style="font-size: 11px; color: #888;">
                        ${viewModel.businessUpdateModel?.businessPhoneNumber ?: ""}<br>
                        ${viewModel.businessUpdateModel?.businessWebsite ?: ""}
                    </div>
                    <div class="signature-area">
        """.trimIndent())

        viewModel.signatureObj?.signatureBitmap?.let { sig ->
            val sigBase64 = utility.encodeToBase64(sig)
            htmlContent.append("""
                <img class="signature-img" src="data:image/png;base64,$sigBase64">
                <div class="label">Authorized Signature</div>
            """.trimIndent())
        }

        htmlContent.append("</div></div>")

        // Photos
        if (!viewModel.photosForInvoice.isNullOrEmpty()) {
            htmlContent.append("<div class='label' style='margin-top: 30px;'>Attachments</div>")
            htmlContent.append("<div class='photos-grid'>")
            viewModel.photosForInvoice?.forEach { photo ->
                val pBase64 = utility.encodeToBase64(photo.invoicePhoto)
                htmlContent.append("""
                    <div class="photo-item">
                        <img class="photo-img" src="data:image/png;base64,$pBase64">
                        <div class="photo-desc">${photo.invoicePhotoDescription ?: ""}</div>
                    </div>
                """.trimIndent())
            }
            htmlContent.append("</div>")
        }

        htmlContent.append("</div></body></html>")

        binding?.webView?.loadDataWithBaseURL(null, htmlContent.toString(), "text/html", "utf-8", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
