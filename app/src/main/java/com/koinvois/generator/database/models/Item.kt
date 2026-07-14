package com.koinvois.generator.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val itemId: Int,
    @ColumnInfo(name = "item_name") val itemName: String?,
    @ColumnInfo(name = "item_unit_cost") val itemUnitCost: Float?,
    @ColumnInfo(name = "item_taxable") val itemTaxable: Boolean?,
    @ColumnInfo(name = "item_details") val itemDetails: String?
)
