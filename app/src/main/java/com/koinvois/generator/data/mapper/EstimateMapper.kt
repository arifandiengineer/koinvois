package com.koinvois.generator.data.mapper

import com.koinvois.generator.database.models.Estimate as EstimateEntity
import com.koinvois.generator.database.models.EstimateItem as EstimateItemEntity
import com.koinvois.generator.database.models.EstimatePhoto as EstimatePhotoEntity
import com.koinvois.generator.domain.model.Estimate
import com.koinvois.generator.domain.model.EstimateItem
import com.koinvois.generator.domain.model.EstimatePhoto

fun EstimateEntity.toDomain(): Estimate = Estimate(
    estimateId = estimateId,
    estimateNumber = estimateNumber,
    estimateDate = estimateDate,
    estimatePoNumber = estimatePoNumber,
    estimateSubtotal = estimateSubtotal,
    estimateDiscountType = estimateDiscountType,
    estimateDiscountAmount = estimateDiscountAmount,
    discountTotalAmount = discountTotalAmount,
    estimateTaxType = estimateTaxType,
    estimateTaxLabel = estimateTaxLabel,
    estimateTaxRate = estimateTaxRate,
    estimateTaxInclusive = estimateTaxInclusive,
    estimateTotal = estimateTotal,
    estimateSignature = estimateSignature,
    signatureDate = signatureDate,
    estimateNotes = estimateNotes,
    estimateStatus = estimateStatus,
    clientPK = clientPK,
    estimateClientName = estimateClientName,
    estimateClientEmail = estimateClientEmail,
    estimateClientMobile = estimateClientMobile,
    estimateClientPhone = estimateClientPhone,
    estimateClientFax = estimateClientFax,
    estimateClientContact = estimateClientContact,
    estimateClientAddress1 = estimateClientAddress1,
    estimateClientAddress2 = estimateClientAddress2,
    estimateClientAddress3 = estimateClientAddress3,
)

fun Estimate.toEntity(): EstimateEntity = EstimateEntity(
    estimateId = estimateId,
    estimateNumber = estimateNumber,
    estimateDate = estimateDate,
    estimatePoNumber = estimatePoNumber,
    estimateSubtotal = estimateSubtotal,
    estimateDiscountType = estimateDiscountType,
    estimateDiscountAmount = estimateDiscountAmount,
    discountTotalAmount = discountTotalAmount,
    estimateTaxType = estimateTaxType,
    estimateTaxLabel = estimateTaxLabel,
    estimateTaxRate = estimateTaxRate,
    estimateTaxInclusive = estimateTaxInclusive,
    estimateTotal = estimateTotal,
    estimateSignature = estimateSignature,
    signatureDate = signatureDate,
    estimateNotes = estimateNotes,
    estimateStatus = estimateStatus,
    clientPK = clientPK,
    estimateClientName = estimateClientName,
    estimateClientEmail = estimateClientEmail,
    estimateClientMobile = estimateClientMobile,
    estimateClientPhone = estimateClientPhone,
    estimateClientFax = estimateClientFax,
    estimateClientContact = estimateClientContact,
    estimateClientAddress1 = estimateClientAddress1,
    estimateClientAddress2 = estimateClientAddress2,
    estimateClientAddress3 = estimateClientAddress3,
)

fun List<EstimateEntity>.toDomain(): List<Estimate> = map { it.toDomain() }

fun EstimateItemEntity.toDomain(): EstimateItem = EstimateItem(
    estimateItemId = estimateItemId,
    estimateIdFK = estimateIdFK,
    estimateItemName = estimateItemName,
    estimateItemUnitCost = estimateItemUnitCost,
    estimateItemQuantity = estimateItemQuantity,
    itemDiscountType = itemDiscountType,
    itemDiscountAmount = itemDiscountAmount,
    estimateItemTaxable = estimateItemTaxable,
    estimateItemDetails = estimateItemDetails,
    itemTotal = itemTotal,
    itemTotalDiscount = itemTotalDiscount,
    itemTaxRate = itemTaxRate,
)

fun EstimateItem.toEntity(): EstimateItemEntity = EstimateItemEntity(
    estimateItemId = estimateItemId,
    estimateIdFK = estimateIdFK,
    estimateItemName = estimateItemName,
    estimateItemUnitCost = estimateItemUnitCost,
    estimateItemQuantity = estimateItemQuantity,
    itemDiscountType = itemDiscountType,
    itemDiscountAmount = itemDiscountAmount,
    estimateItemTaxable = estimateItemTaxable,
    estimateItemDetails = estimateItemDetails,
    itemTotal = itemTotal,
    itemTotalDiscount = itemTotalDiscount,
    itemTaxRate = itemTaxRate,
)

fun List<EstimateItemEntity>.toDomainItems(): List<EstimateItem> = map { it.toDomain() }

fun EstimatePhotoEntity.toDomain(): EstimatePhoto = EstimatePhoto(
    estimatePhotoId = estimatePhotoId,
    estimateIdFK = estimateIdFK,
    estimatePhoto = estimatePhoto,
    estimatePhotoDescription = estimatePhotoDescription,
    estimatePhotoAdditionalDetails = estimatePhotoAdditionalDetails,
)

fun EstimatePhoto.toEntity(): EstimatePhotoEntity = EstimatePhotoEntity(
    estimatePhotoId = estimatePhotoId,
    estimateIdFK = estimateIdFK,
    estimatePhoto = estimatePhoto,
    estimatePhotoDescription = estimatePhotoDescription,
    estimatePhotoAdditionalDetails = estimatePhotoAdditionalDetails,
)

fun List<EstimatePhotoEntity>.toDomainPhotos(): List<EstimatePhoto> = map { it.toDomain() }