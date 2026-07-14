package com.koinvois.generator.domain.model

import android.graphics.Bitmap

data class PersonalBusiness(
    val Id: Int,
    val businessLogo: Bitmap?,
    val businessName: String?,
    val businessOwnerName: String?,
    val businessNumber: String?,
    val businessAddress1: String?,
    val businessAddress2: String?,
    val businessAddress3: String?,
    val businessEmail: String?,
    val businessPhoneNumber: Int?,
    val businessMobileNumber: Int?,
    val businessWebsite: String?
)