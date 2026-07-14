package com.koinvois.generator.database.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EstimatePhoto(
    @PrimaryKey(autoGenerate = true) val estimatePhotoId: Int? = null,
    @ColumnInfo(name = "estimate_id_fk") val estimateIdFK: Int? = null,
    @ColumnInfo(name = "estimate_photo") val estimatePhoto: Bitmap?,
    @ColumnInfo(name = "estimate_photo_description") val estimatePhotoDescription: String?,
    @ColumnInfo(name = "estimate_photo_additional_details") val estimatePhotoAdditionalDetails: String?,
)

