package com.koinvois.generator.data_models

data class ItemForInvoice(
    val itemName: String?,
    val itemUnitCost: Float?,
    val itemQuantity: Int?,
    val itemDiscountType: String?,
    val itemDiscountAmount: Float?,
    val itemTaxable: Boolean?,
    val itemDetails: String?,
    val itemTotal: Float?,
    val itemTotalDiscount: Float?,
    val itemTaxRate: Float?
)
