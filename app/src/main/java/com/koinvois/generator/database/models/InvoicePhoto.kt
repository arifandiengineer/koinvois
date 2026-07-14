package com.koinvois.generator.database.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvoicePhoto(
    @PrimaryKey(autoGenerate = true) val invoicePhotoId: Int? = null,
    @ColumnInfo(name = "invoice_id_fk") val invoiceIdFK: Int? = null,
    @ColumnInfo(name = "invoice_photo") val invoicePhoto: Bitmap?,
    @ColumnInfo(name = "invoice_photo_description") val invoicePhotoDescription: String?,
    @ColumnInfo(name = "invoice_photo_additional_details") val invoicePhotoAdditionalDetails: String?,
)
