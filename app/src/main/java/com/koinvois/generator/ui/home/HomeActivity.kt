package com.koinvois.generator.ui.home

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.webkit.WebView
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.FragmentHomeBinding

class HomeActivity : BaseActivity<FragmentHomeBinding>() {

    override fun inflateBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(LayoutInflater.from(this))

    private fun createWebPrintJob(webView: WebView) {
        // Get a PrintManager instance
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        // Get a print adapter instance
        val printAdapter = webView.createPrintDocumentAdapter("test")

        // Create a print job with name and adapter instance
        val jobName = getString(R.string.app_name) + " Document"
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }
}
