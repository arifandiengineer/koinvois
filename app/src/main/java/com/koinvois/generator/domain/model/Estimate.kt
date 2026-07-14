package com.koinvois.generator.domain.model

import android.graphics.Bitmap

data class Estimate(
    val estimateId: Int,
    val estimateNumber: Int? = null,
    val estimateDate: String? = null,
    val estimatePoNumber: String? = null,
    val estimateSubtotal: Float? = null,

    val estimateDiscountType: String? = null,
    val estimateDiscountAmount: Float? = null,
    val discountTotalAmount: Float? = null,

    val estimateTaxType: String? = null,
    val estimateTaxLabel: String? = null,
    val estimateTaxRate: Float? = null,
    val estimateTaxInclusive: Boolean? = null,
    val estimateTotal: Float? = null,
    val estimateSignature: Bitmap? = null,
    val signatureDate: String? = null,
    val estimateNotes: String? = null,
    val estimateStatus: String? = null,

    val clientPK: Int? = null,
    val estimateClientName: String? = null,
    val estimateClientEmail: String? = null,
    val estimateClientMobile: Int? = null,
    val estimateClientPhone: Int? = null,
    val estimateClientFax: Int? = null,
    val estimateClientContact: String? = null,
    val estimateClientAddress1: String? = null,
    val estimateClientAddress2: String? = null,
    val estimateClientAddress3: String? = null,
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