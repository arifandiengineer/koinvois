package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum

data class ItemCalculationResult(val itemTotal: Float?, val itemTotalDiscount: Float?)

/**
 * Extracted verbatim from ItemDetailForInvoiceFragment/ItemDetailForEstimateFragment saveOnBack()
 * so both call sites share one implementation. Behavior preserved exactly, including the
 * pre-existing quirk where itemTotalDiscount defaults to 0f (not null) for PERCENTAGE when no
 * discount amount is entered, but to null for FLAT_AMOUNT in the same situation.
 */
object ItemCalculator {

    fun calculateItemTotal(
        unitCost: Float?,
        quantity: Int?,
        discountType: String?,
        discountAmount: Float?
    ): ItemCalculationResult {
        var totalDiscountAmountForItem = 0f

        val itemTotal: Float? = when (discountType) {
            ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall -> {
                if (discountAmount == null) {
                    unitCost?.times(quantity ?: 1)
                } else {
                    val totalCost = unitCost?.times(quantity ?: 1)
                    val totalDiscountAmount = (discountAmount.div(100)).times(totalCost ?: 1f)
                    totalDiscountAmountForItem = totalDiscountAmount
                    totalCost?.minus(totalDiscountAmount)
                }
            }
            ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeSmall -> {
                if (discountAmount == null) {
                    unitCost?.times(quantity ?: 1)
                } else {
                    val totalCost = unitCost?.times(quantity ?: 1)
                    totalCost?.minus(discountAmount)
                }
            }
            else -> {
                0f
            }
        }

        val itemTotalDiscount: Float? = when (discountType) {
            ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall -> totalDiscountAmountForItem
            ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeSmall -> discountAmount
            else -> 0f
        }

        return ItemCalculationResult(itemTotal, itemTotalDiscount)
    }
}
