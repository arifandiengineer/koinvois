package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemTaxTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Test

class TaxCalculatorTest {

    private val onTheTotal = ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital
    private val deducted = ItemTaxTypeEnum.DEDUCTED.taxTypeCapital
    private val perItem = ItemTaxTypeEnum.PER_ITEM.taxTypeCapital
    private val noTax = ItemTaxTypeEnum.NONE.taxTypeCapital

    @Test
    fun `on the total - adds percentage to subtotal`() {
        val result = TaxCalculator.calculateTax(
            taxType = onTheTotal,
            taxRate = 10f,
            taxInclusive = false,
            subTotalAfterDiscount = 1000f
        )
        assertEquals(100f, result.taxAmount)
        assertEquals(100f, result.totalAdjustment)
    }

    @Test
    fun `deducted - subtracts percentage from subtotal`() {
        val result = TaxCalculator.calculateTax(
            taxType = deducted,
            taxRate = 5f,
            taxInclusive = false,
            subTotalAfterDiscount = 1000f
        )
        assertEquals(50f, result.taxAmount)
        assertEquals(-50f, result.totalAdjustment)
    }

    @Test
    fun `tax inclusive - shows tax amount but does not adjust total`() {
        val result = TaxCalculator.calculateTax(
            taxType = onTheTotal,
            taxRate = 10f,
            taxInclusive = true,
            subTotalAfterDiscount = 1100f // 1000 + 100 tax
        )
        assertEquals(110f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }

    @Test
    fun `per item - sums tax from taxable items`() {
        val taxableItems = listOf(
            Pair(10f, 100f), // 10 tax
            Pair(5f, 200f)   // 10 tax
        )
        val result = TaxCalculator.calculateTax(
            taxType = perItem,
            taxRate = null, // ignored for per item
            taxInclusive = false,
            subTotalAfterDiscount = 300f,
            taxableItems = taxableItems
        )
        assertEquals(20f, result.taxAmount)
        assertEquals(20f, result.totalAdjustment)
    }

    @Test
    fun `none - returns zero`() {
        val result = TaxCalculator.calculateTax(
            taxType = noTax,
            taxRate = 10f,
            taxInclusive = false,
            subTotalAfterDiscount = 1000f
        )
        assertEquals(0f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }

    @Test
    fun `null tax type - returns zero`() {
        val result = TaxCalculator.calculateTax(
            taxType = null,
            taxRate = 10f,
            taxInclusive = false,
            subTotalAfterDiscount = 1000f
        )
        assertEquals(0f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }
}
