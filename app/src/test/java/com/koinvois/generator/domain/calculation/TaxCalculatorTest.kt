package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemTaxTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Test

class TaxCalculatorTest {

    private val none = ItemTaxTypeEnum.NONE.taxTypeCapital
    private val onTheTotal = ItemTaxTypeEnum.ON_THE_TOTAL.taxTypeCapital
    private val deducted = ItemTaxTypeEnum.DEDUCTED.taxTypeCapital
    private val perItem = ItemTaxTypeEnum.PER_ITEM.taxTypeCapital

    @Test
    fun `none - always zero amount and adjustment`() {
        val result = TaxCalculator.calculateTax(none, 10f, false, 1000f)
        assertEquals(0f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }

    @Test
    fun `on the total - 10 percent of 1000 adds 100 to total`() {
        val result = TaxCalculator.calculateTax(onTheTotal, 10f, false, 1000f)
        assertEquals(100f, result.taxAmount)
        assertEquals(100f, result.totalAdjustment)
    }

    @Test
    fun `on the total - tax inclusive shows amount but does not adjust total`() {
        val result = TaxCalculator.calculateTax(onTheTotal, 10f, true, 1000f)
        assertEquals(100f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }

    @Test
    fun `deducted - 10 percent of 1000 subtracts 100 from total`() {
        val result = TaxCalculator.calculateTax(deducted, 10f, false, 1000f)
        assertEquals(100f, result.taxAmount)
        assertEquals(-100f, result.totalAdjustment)
    }

    @Test
    fun `per item - sums taxable items at their own rate and adds to total`() {
        val result = TaxCalculator.calculateTax(
            perItem, null, false, 1000f,
            taxableItems = listOf(10f to 500f, 5f to 200f)
        )
        // (10% * 500) + (5% * 200) = 50 + 10 = 60
        assertEquals(60f, result.taxAmount)
        assertEquals(60f, result.totalAdjustment)
    }

    @Test
    fun `per item - empty list returns zero`() {
        val result = TaxCalculator.calculateTax(perItem, null, false, 1000f)
        assertEquals(0f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }

    @Test
    fun `null subtotal treated as zero for on the total`() {
        val result = TaxCalculator.calculateTax(onTheTotal, 10f, false, null)
        assertEquals(0f, result.taxAmount)
    }

    @Test
    fun `unknown tax type returns zero`() {
        val result = TaxCalculator.calculateTax("something else", 10f, false, 1000f)
        assertEquals(0f, result.taxAmount)
        assertEquals(0f, result.totalAdjustment)
    }
}
