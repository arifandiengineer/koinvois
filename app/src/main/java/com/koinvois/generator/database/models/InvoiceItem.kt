package com.koinvois.generator.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvoiceItem(
    @PrimaryKey(autoGenerate = true) val invoiceItemId: Int? = null,
    @ColumnInfo(name = "invoice_id_fk") val invoiceIdFK: Int? = null,
    @ColumnInfo(name = "invoice_item_name") val invoiceItemName: String?,
    @ColumnInfo(name = "invoice_item_unit_cost") val invoiceItemUnitCost: Float?,
    @ColumnInfo(name = "invoice_item_quantity") val invoiceItemQuantity: Int?,
    @ColumnInfo(name = "invoice_item_discount_type") val itemDiscountType: String?,
    @ColumnInfo(name = "invoice_item_discount_amount") val itemDiscountAmount: Float?,
    @ColumnInfo(name = "invoice_item_taxable") val invoiceItemTaxable: Boolean?,
    @ColumnInfo(name = "invoice_item_details") val invoiceItemDetails: String?,
    @ColumnInfo(name = "invoice_item_total") val itemTotal: Float?,
    @ColumnInfo(name = "invoice_item_total_discount") val itemTotalDiscount: Float?,
    @ColumnInfo(name = "invoice_item_tax_rate") val itemTaxRate: Float?
)
