package com.koinvois.generator.ui.invoices

import com.koinvois.generator.data_models.InvoicePaymentsModel
import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.database.models.Item
import com.koinvois.generator.database.models.InvoiceItem
import com.koinvois.generator.database.models.InvoicePhoto
import com.koinvois.generator.database.models.PersonalBusiness
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Process-lifetime holder for the in-progress invoice draft, shared across the
 * invoice edit Activities (each screen has its own InvoiceMainViewModel instance
 * via `by viewModels()`, but they all inject this same @Singleton so the draft
 * stays consistent across Activity boundaries the way hiltNavGraphViewModels
 * used to keep it consistent across Fragment destinations in one nav graph).
 */
@javax.inject.Singleton
class InvoiceDraftState @javax.inject.Inject constructor() {

    var invoicePrimaryId: Long? = null
    var invoiceNumber: Int? = null
    var invoiceDate: String? = null
    var invoiceTerms: String? = null
    var invoiceDueDate: String? = null
    var invoicePoNumber: String? = null
    var businessUpdateModel: PersonalBusiness? = null
    var selectedClient: Client? = null
    var selectedItemsList: ArrayList<InvoiceItem>? = arrayListOf()
    var currentInvoiceItem: InvoiceItem? = null
    var currentSelectedPhoto: InvoicePhoto? = null
    var invoiceSubTotal: Float? = null

    var discountType: String? = null
    var discountAmount: Float? = null
    var discountTotalAmount: Float? = null
    var taxType: String? = null
    var taxLabel: String? = null
    var taxRate: Float? = null
    var taxInclusive: Boolean? = null

    val invoiceTotalFlow = MutableStateFlow(0f)
    var invoiceTotal: Float
        get() = invoiceTotalFlow.value
        set(value) { invoiceTotalFlow.value = value }

    var taxAmount: Float = 0f

    val invoicePaymentsFlow = MutableStateFlow<List<InvoicePaymentsModel>>(emptyList())

    var photosForInvoice: ArrayList<InvoicePhoto>? = arrayListOf()
    var invoicePaymentInstructions: String? = null
    var signatureObj: Signature? = null
    var invoiceNotes: String? = null
    var invoiceStatus: String? = null

    var allClients: ArrayList<Client>? = null
    var allItems: ArrayList<Item>? = null
}
