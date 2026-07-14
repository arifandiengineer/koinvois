package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum

/**
 * Uses discountTypeCapital (invoice/estimate-level discount type), distinct from
 * ItemCalculator which uses discountTypeSmall (per-item discount type).
 *
 * Returns the discount AMOUNT to subtract from the subtotal (not the post-discount
 * subtotal), consistently across all discount types.
 */
object DiscountCalculator {

    fun calculateDiscountTotal(
        subTotal: Float?,
        discountType: String?,
        discountAmount: Float?
    ): Float? {
        return when (discountType) {
            ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeCapital -> {
                discountAmount ?: 0f
            }
            ItemDiscountTypeEnum.PERCENTAGE.discountTypeCapital -> {
                (discountAmount?.div(100) ?: 0f).times(subTotal ?: 1f)
            }
            ItemDiscountTypeEnum.NO_DISCOUNT.discountTypeCapital -> {
                0f
            }
            else -> {
                0f
            }
        }
    }
}
