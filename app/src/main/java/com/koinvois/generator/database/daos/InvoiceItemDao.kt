package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.InvoiceItem

@Dao
interface InvoiceItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoiceItem(invoiceItem: InvoiceItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItems(invoiceItems: List<InvoiceItem>)

    @Query("SELECT * FROM invoiceItem")
    suspend fun getAllInvoiceItems(): List<InvoiceItem>

    @Query("SELECT * FROM invoiceItem WHERE invoice_id_fk = :invoiceId")
    suspend fun getInvoiceItemsByInvoiceId(invoiceId: Int): List<InvoiceItem>

    @Update
    suspend fun updateInvoiceItem(vararg invoiceItem: InvoiceItem)

    @Delete
    suspend fun deleteInvoiceItem(vararg invoiceItem: InvoiceItem)

    @Query("DELETE FROM InvoiceItem WHERE invoice_id_fk = :invoiceId ")
    suspend fun deleteInvoiceItems(invoiceId: Long)
}