package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemTaxTypeEnum

data class TaxCalculationResult(val taxAmount: Float, val totalAdjustment: Float)

/**
 * Tax types (see ItemTaxTypeEnum): None = no tax. On The Total = rate% of the
 * post-discount subtotal, added on top of the total (e.g. PPN/VAT). Deducted = rate%
 * of the post-discount subtotal, withheld from the total instead of added (e.g. PPh
 * withholding tax). Per Item = each taxable line item taxed individually at its own
 * rate, summed and added to the total. Tax Inclusive means the rate is already baked
 * into the entered prices, so taxAmount is shown for reference only and does not
 * change the total (totalAdjustment stays 0).
 */
object TaxCalculator {

    fun calculateTax(
        taxType: String?,
        taxRate: Float?,
        taxInclusive: Boolean?,
        subTotalAfterDiscount: Float?,
        taxableItems: List<Pair<Float?, Float?>> = emptyList()
    ): TaxCalculationResult {
        val taxAmount = when (taxType) {
            ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital, ItemTaxTypeEnum.DEDUCTED.taxTypeCapital -> {
                (taxRate?.div(100) ?: 0f).times(subTotalAfterDiscount ?: 0f)
            }
            ItemTaxTypeEnum.PER_ITEM.taxTypeCapital -> {
                taxableItems.sumOf { (itemTaxRate, itemTotal) ->
                    ((itemTaxRate?.div(100) ?: 0f).times(itemTotal ?: 0f)).toDouble()
                }.toFloat()
            }
            else -> 0f
        }

        val totalAdjustment = when {
            taxInclusive == true -> 0f
            taxType == ItemTaxTypeEnum.DEDUCTED.taxTypeCapital -> -taxAmount
            taxType == ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital ||
                taxType == ItemTaxTypeEnum.PER_ITEM.taxTypeCapital -> taxAmount
            else -> 0f
        }

        return TaxCalculationResult(taxAmount, totalAdjustment)
    }
}
