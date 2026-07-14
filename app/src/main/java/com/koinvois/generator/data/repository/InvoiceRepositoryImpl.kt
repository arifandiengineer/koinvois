package com.koinvois.generator.data.repository

import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toDomainItems
import com.koinvois.generator.data.mapper.toDomainPhotos
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.database.daos.InvoiceDao
import com.koinvois.generator.database.daos.InvoiceItemDao
import com.koinvois.generator.database.daos.InvoicePhotoDao
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.model.InvoiceItem
import com.koinvois.generator.domain.model.InvoicePhoto
import com.koinvois.generator.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao,
    private val invoicePhotoDao: InvoicePhotoDao
) : InvoiceRepository {

    override fun observeAllInvoices(): Flow<List<Invoice>> =
        invoiceDao.observeAllInvoices().map { it.toDomain() }

    override suspend fun getAllInvoices(): List<Invoice> =
        invoiceDao.getAllInvoices().toDomain()

    override suspend fun getAllPaidInvoices(): List<Invoice> =
        invoiceDao.getAllPaidInvoices().toDomain()

    override suspend fun getInvoiceById(invoiceId: Int): Invoice =
        invoiceDao.getInvoiceById(invoiceId).toDomain()

    override suspend fun insertInvoice(invoice: Invoice) =
        invoiceDao.insertInvoice(invoice.toEntity())

    override suspend fun updateInvoice(invoice: Invoice) =
        invoiceDao.updateInvoice(invoice.toEntity())

    override suspend fun deleteInvoice(invoice: Invoice) =
        invoiceDao.deleteInvoice(invoice.toEntity())

    override suspend fun deleteInvoiceWithID(id: Long) =
        invoiceDao.deleteInvoiceWithID(id)

    override suspend fun getAllInvoiceItems(): List<InvoiceItem> =
        invoiceItemDao.getAllInvoiceItems().toDomainItems()

    override suspend fun getInvoiceItemsByInvoiceId(invoiceId: Int): List<InvoiceItem> =
        invoiceItemDao.getInvoiceItemsByInvoiceId(invoiceId).toDomainItems()

    override suspend fun insertInvoiceItem(invoiceItem: InvoiceItem) =
        invoiceItemDao.insertInvoiceItem(invoiceItem.toEntity())

    override suspend fun insertAllInvoiceItems(invoiceItems: List<InvoiceItem>) =
        invoiceItemDao.insertAllItems(invoiceItems.map { it.toEntity() })

    override suspend fun updateInvoiceItem(invoiceItem: InvoiceItem) =
        invoiceItemDao.updateInvoiceItem(invoiceItem.toEntity())

    override suspend fun deleteInvoiceItem(invoiceItem: InvoiceItem) =
        invoiceItemDao.deleteInvoiceItem(invoiceItem.toEntity())

    override suspend fun deleteInvoiceItems(invoiceId: Long) =
        invoiceItemDao.deleteInvoiceItems(invoiceId)

    override suspend fun getAllInvoicePhotos(): List<InvoicePhoto> =
        invoicePhotoDao.getAllInvoicePhotos().toDomainPhotos()

    override suspend fun getInvoicePhotosByInvoiceId(invoiceId: Int): List<InvoicePhoto> =
        invoicePhotoDao.getInvoicePhotosByInvoiceId(invoiceId).toDomainPhotos()

    override suspend fun insertInvoicePhoto(invoicePhoto: InvoicePhoto) =
        invoicePhotoDao.insertInvoicePhoto(invoicePhoto.toEntity())

    override suspend fun insertInvoicePhotoList(invoicePhotoList: List<InvoicePhoto>) =
        invoicePhotoDao.insertInvoicePhotoList(invoicePhotoList.map { it.toEntity() })

    override suspend fun updateInvoicePhoto(invoicePhoto: InvoicePhoto) =
        invoicePhotoDao.updateInvoicePhoto(invoicePhoto.toEntity())

    override suspend fun deleteInvoicePhoto(invoicePhoto: InvoicePhoto) =
        invoicePhotoDao.deleteInvoicePhoto(invoicePhoto.toEntity())

    override suspend fun deleteInvoicePhotos(invoiceId: Long) =
        invoicePhotoDao.deleteInvoicePhotos(invoiceId)
}