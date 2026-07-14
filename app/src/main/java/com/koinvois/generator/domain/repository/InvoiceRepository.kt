package com.koinvois.generator.domain.repository

import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.model.InvoiceItem
import com.koinvois.generator.domain.model.InvoicePhoto
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun observeAllInvoices(): Flow<List<Invoice>>
    suspend fun getAllInvoices(): List<Invoice>
    suspend fun getAllPaidInvoices(): List<Invoice>
    suspend fun getInvoiceById(invoiceId: Int): Invoice
    suspend fun insertInvoice(invoice: Invoice)
    suspend fun updateInvoice(invoice: Invoice)
    suspend fun deleteInvoice(invoice: Invoice)
    suspend fun deleteInvoiceWithID(id: Long)

    suspend fun getAllInvoiceItems(): List<InvoiceItem>
    suspend fun getInvoiceItemsByInvoiceId(invoiceId: Int): List<InvoiceItem>
    suspend fun insertInvoiceItem(invoiceItem: InvoiceItem)
    suspend fun insertAllInvoiceItems(invoiceItems: List<InvoiceItem>)
    suspend fun updateInvoiceItem(invoiceItem: InvoiceItem)
    suspend fun deleteInvoiceItem(invoiceItem: InvoiceItem)
    suspend fun deleteInvoiceItems(invoiceId: Long)

    suspend fun getAllInvoicePhotos(): List<InvoicePhoto>
    suspend fun getInvoicePhotosByInvoiceId(invoiceId: Int): List<InvoicePhoto>
    suspend fun insertInvoicePhoto(invoicePhoto: InvoicePhoto)
    suspend fun insertInvoicePhotoList(invoicePhotoList: List<InvoicePhoto>)
    suspend fun updateInvoicePhoto(invoicePhoto: InvoicePhoto)
    suspend fun deleteInvoicePhoto(invoicePhoto: InvoicePhoto)
    suspend fun deleteInvoicePhotos(invoiceId: Long)
}