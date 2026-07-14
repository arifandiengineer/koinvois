package com.koinvois.generator.domain.model

import android.graphics.Bitmap

data class Invoice(
    val invoiceId: Int,
    val invoiceNumber: Int?,
    val invoiceDate: String?,
    val invoiceTerms: String?,
    val invoiceDueDate: String?,
    val invoicePoNumber: String?,
    val invoiceSubtotal: Float?,

    val invoiceDiscountType: String?,
    val invoiceDiscountAmount: Float?,
    val discountTotalAmount: Float?,

    val invoiceTaxType: String?,
    val invoiceTaxLabel: String?,
    val invoiceTaxRate: Float?,
    val invoiceTaxInclusive: Boolean?,
    val invoiceTotal: Float?,
    val invoicePaymentInstruction: String?,
    val invoiceSignature: Bitmap?,
    val signatureDate: String?,
    val invoiceNotes: String?,
    val invoiceStatus: String?,

    val clientPK: Int?,
    val invoiceClientName: String?,
    val invoiceClientEmail: String?,
    val invoiceClientMobile: Int?,
    val invoiceClientPhone: Int?,
    val invoiceClientFax: Int?,
    val invoiceClientContact: String?,
    val invoiceClientAddress1: String?,
    val invoiceClientAddress2: String?,
    val invoiceClientAddress3: String?,
)

data class InvoiceItem(
    val invoiceItemId: Int? = null,
    val invoiceIdFK: Int? = null,
    val invoiceItemName: String?,
    val invoiceItemUnitCost: Float?,
    val invoiceItemQuantity: Int?,
    val itemDiscountType: String?,
    val itemDiscountAmount: Float?,
    val invoiceItemTaxable: Boolean?,
    val invoiceItemDetails: String?,
    val itemTotal: Float?,
    val itemTotalDiscount: Float?,
    val itemTaxRate: Float?
)

data class InvoicePhoto(
    val invoicePhotoId: Int? = null,
    val invoiceIdFK: Int? = null,
    val invoicePhoto: Bitmap?,
    val invoicePhotoDescription: String?,
    val invoicePhotoAdditionalDetails: String?,
)