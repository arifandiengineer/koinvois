package com.koinvois.generator.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.*
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.*
import com.koinvois.generator.ui.lock.SavePasswordActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubActivitiesAuditTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun launchInvoiceSubActivities() {
        ActivityScenario.launch(EditBusinessDetailsFromInvoiceActivity::class.java).use { }
        ActivityScenario.launch(InvoicePaymentsListActivity::class.java).use { }
        ActivityScenario.launch(InvoiceInformationActivity::class.java).use { }
        ActivityScenario.launch(DiscountActivity::class.java).use { }
        ActivityScenario.launch(ClientListForInvoiceActivity::class.java).use { }
        ActivityScenario.launch(ItemsListForInvoiceActivity::class.java).use { }
        ActivityScenario.launch(TaxActivity::class.java).use { }
        ActivityScenario.launch(SignatureActivity::class.java).use { }
        
        val itemIntent = com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ItemDetailForInvoiceActivity.newIntent(context, "new")
        ActivityScenario.launch<com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ItemDetailForInvoiceActivity>(itemIntent).use { }
        
        val clientIntent = com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ClientDetailForInvoiceActivity.newIntent(context)
        ActivityScenario.launch<com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.ClientDetailForInvoiceActivity>(clientIntent).use { }
        
        val photoIntent = com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.AddPhotoToInvoiceActivity.newIntent(context, "new")
        ActivityScenario.launch<com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments.AddPhotoToInvoiceActivity>(photoIntent).use { }
        
        ActivityScenario.launch(InvoiceAddPaymentActivity::class.java).use { }
    }

    @Test
    fun launchEstimateSubActivities() {
        ActivityScenario.launch(EstimateSignatureActivity::class.java).use { }
        ActivityScenario.launch(EstimateInformationActivity::class.java).use { }
        ActivityScenario.launch(EditBusinessDetailsFromEstimateActivity::class.java).use { }
        ActivityScenario.launch(ClientListForEstimateActivity::class.java).use { }
        ActivityScenario.launch(EstimateDiscountActivity::class.java).use { }
        ActivityScenario.launch(EstimateTaxActivity::class.java).use { }
        ActivityScenario.launch(ItemListForEstimateActivity::class.java).use { }

        val itemIntent = com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ItemDetailForEstimateActivity.newIntent(context, "new")
        ActivityScenario.launch<com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ItemDetailForEstimateActivity>(itemIntent).use { }

        val clientIntent = com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ClientDetailForEstimateActivity.newIntent(context)
        ActivityScenario.launch<com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.ClientDetailForEstimateActivity>(clientIntent).use { }
        
        val photoIntent = com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.AddPhotoToEstimateActivity.newIntent(context, "new")
        ActivityScenario.launch<com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments.AddPhotoToEstimateActivity>(photoIntent).use { }
    }

    @Test
    fun launchSecuritySubActivities() {
        val intent = SavePasswordActivity.newIntent(context, "1234")
        ActivityScenario.launch<SavePasswordActivity>(intent).use { }
    }
}
