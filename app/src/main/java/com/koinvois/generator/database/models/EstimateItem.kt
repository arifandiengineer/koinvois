package com.koinvois.generator.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EstimateItem(
    @PrimaryKey(autoGenerate = true) val estimateItemId: Int? = null,
    @ColumnInfo(name = "estimate_id_fk") val estimateIdFK: Int? = null,
    @ColumnInfo(name = "estimate_item_name") val estimateItemName: String?,
    @ColumnInfo(name = "estimate_item_unit_cost") val estimateItemUnitCost: Float?,
    @ColumnInfo(name = "estimate_item_quantity") val estimateItemQuantity: Int?,
    @ColumnInfo(name = "estimate_item_discount_type") val itemDiscountType: String?,
    @ColumnInfo(name = "estimate_item_discount_amount") val itemDiscountAmount: Float?,
    @ColumnInfo(name = "estimate_item_taxable") val estimateItemTaxable: Boolean?,
    @ColumnInfo(name = "estimate_item_details") val estimateItemDetails: String?,
    @ColumnInfo(name = "estimate_item_total") val itemTotal: Float?,
    @ColumnInfo(name = "estimate_item_total_discount") val itemTotalDiscount: Float?,
    @ColumnInfo(name = "estimate_item_tax_rate") val itemTaxRate: Float?
)