package com.koinvois.generator.data.mapper

import com.koinvois.generator.database.models.Item as ItemEntity
import com.koinvois.generator.domain.model.Item

fun ItemEntity.toDomain(): Item = Item(
    itemId = itemId,
    itemName = itemName,
    itemUnitCost = itemUnitCost,
    itemTaxable = itemTaxable,
    itemDetails = itemDetails,
)

fun Item.toEntity(): ItemEntity = ItemEntity(
    itemId = itemId,
    itemName = itemName,
    itemUnitCost = itemUnitCost,
    itemTaxable = itemTaxable,
    itemDetails = itemDetails,
)

fun List<ItemEntity>.toDomain(): List<Item> = map { it.toDomain() }