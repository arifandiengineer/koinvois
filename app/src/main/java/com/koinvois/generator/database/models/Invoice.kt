package com.koinvois.generator.database.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invoice(
    @PrimaryKey val invoiceId: Int,
    @ColumnInfo(name = "invoice_number") val invoiceNumber: Int?,
    @ColumnInfo(name = "invoice_date") val invoiceDate: String?,
    @ColumnInfo(name = "invoice_terms") val invoiceTerms: String?,
    @ColumnInfo(name = "invoice_due_date") val invoiceDueDate: String?,
    @ColumnInfo(name = "invoice_po_number") val invoicePoNumber: String?,
    @ColumnInfo(name = "invoice_subtotal") val invoiceSubtotal: Float?,

    @ColumnInfo(name = "invoice_discount_type") val invoiceDiscountType: String?,
    @ColumnInfo(name = "invoice_discount_amount") val invoiceDiscountAmount: Float?,
//    @ColumnInfo(name = "invoice_discount_percentage") val invoiceDiscountPercentage: Float?,
    @ColumnInfo(name = "invoice_discount_total_amount") val discountTotalAmount: Float?,

    @ColumnInfo(name = "invoice_tax_type") val invoiceTaxType: String?,
    @ColumnInfo(name = "invoice_tax_label") val invoiceTaxLabel: String?,
    @ColumnInfo(name = "invoice_tax_rate") val invoiceTaxRate: Float?,
    @ColumnInfo(name = "invoice_tax_inclusive") val invoiceTaxInclusive: Boolean?,
    @ColumnInfo(name = "invoice_total") val invoiceTotal: Float?,
    @ColumnInfo(name = "invoice_payment_instruction") val invoicePaymentInstruction: String?,
    @ColumnInfo(name = "invoice_signature") val invoiceSignature: Bitmap?,
    @ColumnInfo(name = "signature_date") val signatureDate: String?,
    @ColumnInfo(name = "invoice_notes") val invoiceNotes: String?,
    @ColumnInfo(name = "invoice_status") val invoiceStatus: String?,

    @ColumnInfo(name = "client_pk") val clientPK: Int?,
    @ColumnInfo(name = "client_name") val invoiceClientName: String?,
    @ColumnInfo(name = "client_email") val invoiceClientEmail: String?,
    @ColumnInfo(name = "client_mobile") val invoiceClientMobile: Int?,
    @ColumnInfo(name = "client_phone") val invoiceClientPhone: Int?,
    @ColumnInfo(name = "client_fax") val invoiceClientFax: Int?,
    @ColumnInfo(name = "client_contact") val invoiceClientContact: String?,
    @ColumnInfo(name = "client_address1") val invoiceClientAddress1: String?,
    @ColumnInfo(name = "client_address2") val invoiceClientAddress2: String?,
    @ColumnInfo(name = "client_address3") val invoiceClientAddress3: String?,
)
