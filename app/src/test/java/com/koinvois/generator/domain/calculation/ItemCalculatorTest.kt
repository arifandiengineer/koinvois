package com.koinvois.generator.domain.calculation

import com.koinvois.generator.utilities.enums.ItemDiscountTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Golden-value characterization tests for ItemCalculator, capturing the exact behavior of the
 * inline calculation that used to live in ItemDetailForInvoiceFragment/ItemDetailForEstimateFragment
 * saveOnBack(). These guard against accidental calculation changes during UI/UX modernization
 * (Input Component Matrix migration) — they assert what the code currently produces, not what
 * would be mathematically "correct".
 */
class ItemCalculatorTest {

    private val percentage = ItemDiscountTypeEnum.PERCENTAGE.discountTypeSmall
    private val flatAmount = ItemDiscountTypeEnum.FLAT_AMOUNT.discountTypeSmall

    @Test
    fun `no discount type - falls into else branch - returns ZERO total (pre-existing quirk, not raw cost)`() {
        // "no discount" is not PERCENTAGE.discountTypeSmall or FLAT_AMOUNT.discountTypeSmall, so the
        // original when() falls to `else -> 0f` — itemTotal is 0, NOT the raw unitCost*quantity.
        // This is surprising but is the actual current behavior; preserved verbatim.
        val result = ItemCalculator.calculateItemTotal(
            unitCost = 50f,
            quantity = 3,
            discountType = "no discount",
            discountAmount = null
        )
        assertEquals(0f, result.itemTotal)
        assertEquals(0f, result.itemTotalDiscount)
    }

    @Test
    fun `percentage discount - null discount amount - returns raw cost, zero discount`() {
        val result = ItemCalculator.calculateItemTotal(
            unitCost = 100f,
            quantity = 2,
            discountType = percentage,
            discountAmount = null
        )
        assertEquals(200f, result.itemTotal)
        assertEquals(0f, result.itemTotalDiscount)
    }

    @Test
    fun `percentage discount - 10 percent off 200 - returns 180 total, 20 discount`() {
        val result = ItemCalculator.calculateItemTotal(
            unitCost = 100f,
            quantity = 2,
            discountType = percentage,
            discountAmount = 10f
        )
        assertEquals(180f, result.itemTotal)
        assertEquals(20f, result.itemTotalDiscount)
    }

    @Test
    fun `flat amount discount - null discount amount - returns raw cost, null discount`() {
        val result = ItemCalculator.calculateItemTotal(
            unitCost = 100f,
            quantity = 2,
            discountType = flatAmount,
            discountAmount = null
        )
        assertEquals(200f, result.itemTotal)
        assertNull(result.itemTotalDiscount)
    }

    @Test
    fun `flat amount discount - 30 off 200 - returns 170 total, 30 discount`() {
        val result = ItemCalculator.calculateItemTotal(
            unitCost = 100f,
            quantity = 2,
            discountType = flatAmount,
            discountAmount = 30f
        )
        assertEquals(170f, result.itemTotal)
        assertEquals(30f, result.itemTotalDiscount)
    }

    @Test
    fun `null unit cost - percentage discount - propagates null total`() {
        val result = ItemCalculator.calculateItemTotal(
            unitCost = null,
            quantity = 2,
            discountType = percentage,
            discountAmount = 10f
        )
        // totalCost = null, but totalDiscountAmountForItem is still computed as
        // (10/100) * (totalCost ?: 1f) = 0.1 * 1f = 0.1 before the null propagates itemTotal to null.
        assertNull(result.itemTotal)
        assertEquals(0.1f, result.itemTotalDiscount)
    }

    @Test
    fun `null quantity - percentage discount - defaults quantity to 1`() {
        val result = ItemCalculator.calculateItemTotal(
            unitCost = 50f,
            quantity = null,
            discountType = percentage,
            discountAmount = 20f
        )
        // totalCost = 50*1 = 50, discount = 20% of 50 = 10, total = 40
        assertEquals(40f, result.itemTotal)
        assertEquals(10f, result.itemTotalDiscount)
    }
}
