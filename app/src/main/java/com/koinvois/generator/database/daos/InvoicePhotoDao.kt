package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.InvoicePhoto

@Dao
interface InvoicePhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoicePhoto(invoicePhoto: InvoicePhoto)

    @Insert
    suspend fun insertInvoicePhotoList(invoicePhotoList: List<InvoicePhoto>)

    @Query("SELECT * FROM invoicePhoto")
    suspend fun getAllInvoicePhotos(): List<InvoicePhoto>

    @Query("SELECT * FROM InvoicePhoto WHERE invoice_id_fk = :invoiceId")
    suspend fun getInvoicePhotosByInvoiceId(invoiceId: Int): List<InvoicePhoto>

    @Update
    suspend fun updateInvoicePhoto(vararg invoicePhoto: InvoicePhoto)

    @Delete
    suspend fun deleteInvoicePhoto(vararg invoicePhoto: InvoicePhoto)

    @Query("DELETE FROM InvoicePhoto WHERE invoice_id_fk = :invoiceId")
    suspend fun deleteInvoicePhotos(invoiceId: Long)
}