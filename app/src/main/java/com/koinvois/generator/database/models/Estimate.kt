package com.koinvois.generator.database.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Estimate(
    @PrimaryKey val estimateId: Int,
    @ColumnInfo(name = "estimate_number") val estimateNumber: Int? = null,
    @ColumnInfo(name = "estimate_date") val estimateDate: String? = null,
    @ColumnInfo(name = "estimate_po_number") val estimatePoNumber: String? = null,
    @ColumnInfo(name = "estimate_subtotal") val estimateSubtotal: Float? = null,

    @ColumnInfo(name = "estimate_discount_type") val estimateDiscountType: String? = null,
    @ColumnInfo(name = "estimate_discount_amount") val estimateDiscountAmount: Float? = null,
    @ColumnInfo(name = "estimate_discount_total_amount") val discountTotalAmount: Float? = null,

    @ColumnInfo(name = "estimate_tax_type") val estimateTaxType: String? = null,
    @ColumnInfo(name = "estimate_tax_label") val estimateTaxLabel: String? = null,
    @ColumnInfo(name = "estimate_tax_rate") val estimateTaxRate: Float? = null,
    @ColumnInfo(name = "estimate_tax_inclusive") val estimateTaxInclusive: Boolean? = null,
    @ColumnInfo(name = "estimate_total") val estimateTotal: Float? = null,
    @ColumnInfo(name = "estimate_signature") val estimateSignature: Bitmap? = null,
    @ColumnInfo(name = "signature_date") val signatureDate: String? = null,
    @ColumnInfo(name = "estimate_notes") val estimateNotes: String? = null,
    @ColumnInfo(name = "estimate_status") val estimateStatus: String? = null,

    @ColumnInfo(name = "client_pk") val clientPK: Int? = null,
    @ColumnInfo(name = "client_name") val estimateClientName: String? = null,
    @ColumnInfo(name = "client_email") val estimateClientEmail: String? = null,
    @ColumnInfo(name = "client_mobile") val estimateClientMobile: Int? = null,
    @ColumnInfo(name = "client_phone") val estimateClientPhone: Int? = null,
    @ColumnInfo(name = "client_fax") val estimateClientFax: Int? = null,
    @ColumnInfo(name = "client_contact") val estimateClientContact: String? = null,
    @ColumnInfo(name = "client_address1") val estimateClientAddress1: String? = null,
    @ColumnInfo(name = "client_address2") val estimateClientAddress2: String? = null,
    @ColumnInfo(name = "client_address3") val estimateClientAddress3: String? = null,
)
