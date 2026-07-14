package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Golden-value characterization tests for DiscountCalculator, capturing the exact behavior of the
 * inline calculation that used to live in DiscountFragment/EstimateDiscountFragment saveOnBack().
 */
class DiscountCalculatorTest {

    private val percentage = ItemDiscountTypeEnum.PERCENTAGE.discountTypeCapital
    private val flatAmount = ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeCapital
    private val noDiscount = ItemDiscountTypeEnum.NO_DISCOUNT.discountTypeCapital

    @Test
    fun `flat amount - returns the discount amount itself`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = 500f,
            discountType = flatAmount,
            discountAmount = 50f
        )
        assertEquals(50f, result)
    }

    @Test
    fun `flat amount - null discount amount treated as zero`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = 500f,
            discountType = flatAmount,
            discountAmount = null
        )
        assertEquals(0f, result)
    }

    @Test
    fun `percentage - 10 percent of 500 returns 50`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = 500f,
            discountType = percentage,
            discountAmount = 10f
        )
        assertEquals(50f, result)
    }

    @Test
    fun `percentage - null discount amount returns zero`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = 500f,
            discountType = percentage,
            discountAmount = null
        )
        assertEquals(0f, result)
    }

    @Test
    fun `no discount - always returns zero regardless of subtotal`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = 500f,
            discountType = noDiscount,
            discountAmount = 10f
        )
        assertEquals(0f, result)
    }

    @Test
    fun `unknown discount type - returns zero`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = 500f,
            discountType = "something else",
            discountAmount = 10f
        )
        assertEquals(0f, result)
    }

    @Test
    fun `percentage - null subtotal defaults to 1 in multiplication`() {
        val result = DiscountCalculator.calculateDiscountTotal(
            subTotal = null,
            discountType = percentage,
            discountAmount = 50f
        )
        // (50/100) * 1 = 0.5
        assertEquals(0.5f, result)
    }
}
