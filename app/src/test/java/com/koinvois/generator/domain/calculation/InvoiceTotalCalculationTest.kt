package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import com.koinvois.generator.utilities.enums.ItemTaxTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Audit test to ensure the combined logic of Discount and Tax results in the correct Grand Total.
 * This mirrors the logic in InvoiceMainViewModel.recalculateInvoiceTotal().
 */
class InvoiceTotalCalculationTest {

    @Test
    fun `grand total calculation - complex scenario`() {
        // 1. Subtotal
        val subtotal = 1000f

        // 2. Discount: 10% of 1000 = 100
        val discountType = ItemDiscountTypeEnum.PERCENTAGE.discountTypeCapital
        val discountAmount = 10f
        val discountTotal = DiscountCalculator.calculateDiscountTotal(subtotal, discountType, discountAmount) ?: 0f
        assertEquals(100f, discountTotal)

        val subtotalAfterDiscount = subtotal - discountTotal

        assertEquals(900f, subtotalAfterDiscount)

        // 3. Tax: 10% ON_THE_TOTAL of 900 = 90
        val taxType = ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital
        val taxRate = 10f
        val taxResult = TaxCalculator.calculateTax(
            taxType = taxType,
            taxRate = taxRate,
            taxInclusive = false,
            subTotalAfterDiscount = subtotalAfterDiscount
        )
        assertEquals(90f, taxResult.taxAmount)
        assertEquals(90f, taxResult.totalAdjustment)

        // 4. Grand Total: 900 + 90 = 990
        val grandTotal = subtotalAfterDiscount + taxResult.totalAdjustment
        assertEquals(990f, grandTotal)
    }

    @Test
    fun `grand total calculation - tax inclusive`() {
        val subtotal = 1100f
        val discountTotal = 0f
        val subtotalAfterDiscount = subtotal - discountTotal

        // Tax 10% Inclusive
        val taxResult = TaxCalculator.calculateTax(
            taxType = ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital,
            taxRate = 10f,
            taxInclusive = true,
            subTotalAfterDiscount = subtotalAfterDiscount
        )
        // Tax is 110 (1100 * 0.1), but adjustment is 0
        assertEquals(110f, taxResult.taxAmount)
        assertEquals(0f, taxResult.totalAdjustment)

        val grandTotal = subtotalAfterDiscount + taxResult.totalAdjustment
        assertEquals(1100f, grandTotal)
    }
}
