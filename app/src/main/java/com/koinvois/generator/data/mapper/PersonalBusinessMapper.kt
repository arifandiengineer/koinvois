package com.koinvois.generator.data.mapper

import com.koinvois.generator.database.models.PersonalBusiness as PersonalBusinessEntity
import com.koinvois.generator.domain.model.PersonalBusiness

fun PersonalBusinessEntity.toDomain(): PersonalBusiness = PersonalBusiness(
    Id = Id,
    businessLogo = businessLogo,
    businessName = businessName,
    businessOwnerName = businessOwnerName,
    businessNumber = businessNumber,
    businessAddress1 = businessAddress1,
    businessAddress2 = businessAddress2,
    businessAddress3 = businessAddress3,
    businessEmail = businessEmail,
    businessPhoneNumber = businessPhoneNumber,
    businessMobileNumber = businessMobileNumber,
    businessWebsite = businessWebsite,
)

fun PersonalBusiness.toEntity(): PersonalBusinessEntity = PersonalBusinessEntity(
    Id = Id,
    businessLogo = businessLogo,
    businessName = businessName,
    businessOwnerName = businessOwnerName,
    businessNumber = businessNumber,
    businessAddress1 = businessAddress1,
    businessAddress2 = businessAddress2,
    businessAddress3 = businessAddress3,
    businessEmail = businessEmail,
    businessPhoneNumber = businessPhoneNumber,
    businessMobileNumber = businessMobileNumber,
    businessWebsite = businessWebsite,
)

fun List<PersonalBusinessEntity>.toDomain(): List<PersonalBusiness> = map { it.toDomain() }