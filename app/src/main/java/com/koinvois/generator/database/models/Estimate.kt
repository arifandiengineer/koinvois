package com.koinvois.generator.database.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Estimate(
    @PrimaryKey val estimateId: Int,
    @ColumnInfo(name = "estimate_number") val estimateNumber: Int?,
    @ColumnInfo(name = "estimate_date") val estimateDate: String?,
    @ColumnInfo(name = "estimate_po_number") val estimatePoNumber: String?,
    @ColumnInfo(name = "estimate_subtotal") val estimateSubtotal: Float?,

    @ColumnInfo(name = "estimate_discount_type") val estimateDiscountType: String?,
    @ColumnInfo(name = "estimate_discount_amount") val estimateDiscountAmount: Float?,
    @ColumnInfo(name = "estimate_discount_total_amount") val discountTotalAmount: Float?,

    @ColumnInfo(name = "estimate_tax_type") val estimateTaxType: String?,
    @ColumnInfo(name = "estimate_tax_label") val estimateTaxLabel: String?,
    @ColumnInfo(name = "estimate_tax_rate") val estimateTaxRate: Float?,
    @ColumnInfo(name = "estimate_tax_inclusive") val estimateTaxInclusive: Boolean?,
    @ColumnInfo(name = "estimate_total") val estimateTotal: Float?,
    @ColumnInfo(name = "estimate_signature") val estimateSignature: Bitmap?,
    @ColumnInfo(name = "signature_date") val signatureDate: String?,
    @ColumnInfo(name = "estimate_notes") val estimateNotes: String?,
    @ColumnInfo(name = "estimate_status") val estimateStatus: String?,

    @ColumnInfo(name = "client_pk") val clientPK: Int?,
    @ColumnInfo(name = "client_name") val estimateClientName: String?,
    @ColumnInfo(name = "client_email") val estimateClientEmail: String?,
    @ColumnInfo(name = "client_mobile") val estimateClientMobile: Int?,
    @ColumnInfo(name = "client_phone") val estimateClientPhone: Int?,
    @ColumnInfo(name = "client_fax") val estimateClientFax: Int?,
    @ColumnInfo(name = "client_contact") val estimateClientContact: String?,
    @ColumnInfo(name = "client_address1") val estimateClientAddress1: String?,
    @ColumnInfo(name = "client_address2") val estimateClientAddress2: String?,
    @ColumnInfo(name = "client_address3") val estimateClientAddress3: String?,
)
