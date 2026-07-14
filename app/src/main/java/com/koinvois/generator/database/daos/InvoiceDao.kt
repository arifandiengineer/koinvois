package com.koinvois.generator.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.koinvois.generator.database.models.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert
    suspend fun insertInvoice(invoice: Invoice)

    @Query("SELECT * FROM invoice")
    suspend fun getAllInvoices(): List<Invoice>

    @Query("SELECT * FROM invoice")
    fun observeAllInvoices(): Flow<List<Invoice>>

    @Query("SELECT * FROM invoice Where invoice_status like 'PAID'")
    suspend fun getAllPaidInvoices(): List<Invoice>

    @Query("SELECT * FROM invoice")
    fun getAllInvoicesLive(): LiveData<List<Invoice>>

    @Query("SELECT * FROM invoice WHERE invoiceId = :invoiceId")
    suspend fun getInvoiceById(invoiceId: Int): Invoice

    @Update
    suspend fun updateInvoice(vararg invoice: Invoice)

    @Delete
    suspend fun deleteInvoice(vararg invoice: Invoice)

    @Query("Delete from invoice where invoiceId = :id")
    suspend fun deleteInvoiceWithID(vararg id: Long)

}