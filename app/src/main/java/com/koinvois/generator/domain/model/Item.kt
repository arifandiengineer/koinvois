package com.koinvois.generator.domain.model

data class Item(
    val itemId: Int,
    val itemName: String?,
    val itemUnitCost: Float?,
    val itemTaxable: Boolean?,
    val itemDetails: String?
)
