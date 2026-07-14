package com.koinvois.generator.domain.model

import android.graphics.Bitmap

data class Estimate(
    val estimateId: Int,
    val estimateNumber: Int?,
    val estimateDate: String?,
    val estimatePoNumber: String?,
    val estimateSubtotal: Float?,

    val estimateDiscountType: String?,
    val estimateDiscountAmount: Float?,
    val discountTotalAmount: Float?,

    val estimateTaxType: String?,
    val estimateTaxLabel: String?,
    val estimateTaxRate: Float?,
    val estimateTaxInclusive: Boolean?,
    val estimateTotal: Float?,
    val estimateSignature: Bitmap?,
    val signatureDate: String?,
    val estimateNotes: String?,
    val estimateStatus: String?,

    val clientPK: Int?,
    val estimateClientName: String?,
    val estimateClientEmail: String?,
    val estimateClientMobile: Int?,
    val estimateClientPhone: Int?,
    val estimateClientFax: Int?,
    val estimateClientContact: String?,
    val estimateClientAddress1: String?,
    val estimateClientAddress2: String?,
    val estimateClientAddress3: String?,
)

data class EstimateItem(
    val estimateItemId: Int? = null,
    val estimateIdFK: Int? = null,
    val estimateItemName: String?,
    val estimateItemUnitCost: Float?,
    val estimateItemQuantity: Int?,
    val itemDiscountType: String?,
    val itemDiscountAmount: Float?,
    val estimateItemTaxable: Boolean?,
    val estimateItemDetails: String?,
    val itemTotal: Float?,
    val itemTotalDiscount: Float?,
    val itemTaxRate: Float?
)

data class EstimatePhoto(
    val estimatePhotoId: Int? = null,
    val estimateIdFK: Int? = null,
    val estimatePhoto: Bitmap?,
    val estimatePhotoDescription: String?,
    val estimatePhotoAdditionalDetails: String?,
)