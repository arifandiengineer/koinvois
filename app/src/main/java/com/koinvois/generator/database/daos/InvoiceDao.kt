package com.koinvois.generator.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.koinvois.generator.database.models.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert
    suspend fun insertInvoice(invoice: Invoice)

    @Query("SELECT invoiceId, invoice_number, invoice_date, invoice_terms, invoice_due_date, invoice_po_number, invoice_subtotal, invoice_discount_type, invoice_discount_amount, invoice_discount_total_amount, invoice_tax_type, invoice_tax_label, invoice_tax_rate, invoice_tax_inclusive, invoice_total, invoice_payment_instruction, NULL as invoice_signature, signature_date, invoice_notes, invoice_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM invoice")
    suspend fun getAllInvoices(): List<Invoice>

    @Query("SELECT invoiceId, invoice_number, invoice_date, invoice_terms, invoice_due_date, invoice_po_number, invoice_subtotal, invoice_discount_type, invoice_discount_amount, invoice_discount_total_amount, invoice_tax_type, invoice_tax_label, invoice_tax_rate, invoice_tax_inclusive, invoice_total, invoice_payment_instruction, NULL as invoice_signature, signature_date, invoice_notes, invoice_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM invoice")
    fun observeAllInvoices(): Flow<List<Invoice>>

    @Query("SELECT invoiceId, invoice_number, invoice_date, invoice_terms, invoice_due_date, invoice_po_number, invoice_subtotal, invoice_discount_type, invoice_discount_amount, invoice_discount_total_amount, invoice_tax_type, invoice_tax_label, invoice_tax_rate, invoice_tax_inclusive, invoice_total, invoice_payment_instruction, NULL as invoice_signature, signature_date, invoice_notes, invoice_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM invoice Where invoice_status like 'PAID'")
    suspend fun getAllPaidInvoices(): List<Invoice>

    @Query("SELECT invoiceId, invoice_number, invoice_date, invoice_terms, invoice_due_date, invoice_po_number, invoice_subtotal, invoice_discount_type, invoice_discount_amount, invoice_discount_total_amount, invoice_tax_type, invoice_tax_label, invoice_tax_rate, invoice_tax_inclusive, invoice_total, invoice_payment_instruction, NULL as invoice_signature, signature_date, invoice_notes, invoice_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM invoice")
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