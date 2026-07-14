package com.koinvois.generator.database.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PersonalBusiness(
    @PrimaryKey val Id: Int,
    @ColumnInfo(name = "business_logo") val businessLogo: Bitmap?,
    @ColumnInfo(name = "business_name") val businessName: String?,
    @ColumnInfo(name = "business_owner_name") val businessOwnerName: String?,
    @ColumnInfo(name = "business_number") val businessNumber: String?,
    @ColumnInfo(name = "business_address1") val businessAddress1: String?,
    @ColumnInfo(name = "business_address2") val businessAddress2: String?,
    @ColumnInfo(name = "business_address3") val businessAddress3: String?,
    @ColumnInfo(name = "business_email") val businessEmail: String?,
    @ColumnInfo(name = "business_phone_number") val businessPhoneNumber: Int?,
    @ColumnInfo(name = "business_mobile_number") val businessMobileNumber: Int?,
    @ColumnInfo(name = "business_website") val businessWebsite: String?
)
