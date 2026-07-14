package com.koinvois.generator.data.mapper

import com.koinvois.generator.database.models.Invoice as InvoiceEntity
import com.koinvois.generator.database.models.InvoiceItem as InvoiceItemEntity
import com.koinvois.generator.database.models.InvoicePhoto as InvoicePhotoEntity
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.model.InvoiceItem
import com.koinvois.generator.domain.model.InvoicePhoto

fun InvoiceEntity.toDomain(): Invoice = Invoice(
    invoiceId = invoiceId,
    invoiceNumber = invoiceNumber,
    invoiceDate = invoiceDate,
    invoiceTerms = invoiceTerms,
    invoiceDueDate = invoiceDueDate,
    invoicePoNumber = invoicePoNumber,
    invoiceSubtotal = invoiceSubtotal,
    invoiceDiscountType = invoiceDiscountType,
    invoiceDiscountAmount = invoiceDiscountAmount,
    discountTotalAmount = discountTotalAmount,
    invoiceTaxType = invoiceTaxType,
    invoiceTaxLabel = invoiceTaxLabel,
    invoiceTaxRate = invoiceTaxRate,
    invoiceTaxInclusive = invoiceTaxInclusive,
    invoiceTotal = invoiceTotal,
    invoicePaymentInstruction = invoicePaymentInstruction,
    invoiceSignature = invoiceSignature,
    signatureDate = signatureDate,
    invoiceNotes = invoiceNotes,
    invoiceStatus = invoiceStatus,
    clientPK = clientPK,
    invoiceClientName = invoiceClientName,
    invoiceClientEmail = invoiceClientEmail,
    invoiceClientMobile = invoiceClientMobile,
    invoiceClientPhone = invoiceClientPhone,
    invoiceClientFax = invoiceClientFax,
    invoiceClientContact = invoiceClientContact,
    invoiceClientAddress1 = invoiceClientAddress1,
    invoiceClientAddress2 = invoiceClientAddress2,
    invoiceClientAddress3 = invoiceClientAddress3,
)

fun Invoice.toEntity(): InvoiceEntity = InvoiceEntity(
    invoiceId = invoiceId,
    invoiceNumber = invoiceNumber,
    invoiceDate = invoiceDate,
    invoiceTerms = invoiceTerms,
    invoiceDueDate = invoiceDueDate,
    invoicePoNumber = invoicePoNumber,
    invoiceSubtotal = invoiceSubtotal,
    invoiceDiscountType = invoiceDiscountType,
    invoiceDiscountAmount = invoiceDiscountAmount,
    discountTotalAmount = discountTotalAmount,
    invoiceTaxType = invoiceTaxType,
    invoiceTaxLabel = invoiceTaxLabel,
    invoiceTaxRate = invoiceTaxRate,
    invoiceTaxInclusive = invoiceTaxInclusive,
    invoiceTotal = invoiceTotal,
    invoicePaymentInstruction = invoicePaymentInstruction,
    invoiceSignature = invoiceSignature,
    signatureDate = signatureDate,
    invoiceNotes = invoiceNotes,
    invoiceStatus = invoiceStatus,
    clientPK = clientPK,
    invoiceClientName = invoiceClientName,
    invoiceClientEmail = invoiceClientEmail,
    invoiceClientMobile = invoiceClientMobile,
    invoiceClientPhone = invoiceClientPhone,
    invoiceClientFax = invoiceClientFax,
    invoiceClientContact = invoiceClientContact,
    invoiceClientAddress1 = invoiceClientAddress1,
    invoiceClientAddress2 = invoiceClientAddress2,
    invoiceClientAddress3 = invoiceClientAddress3,
)

fun List<InvoiceEntity>.toDomain(): List<Invoice> = map { it.toDomain() }

fun InvoiceItemEntity.toDomain(): InvoiceItem = InvoiceItem(
    invoiceItemId = invoiceItemId,
    invoiceIdFK = invoiceIdFK,
    invoiceItemName = invoiceItemName,
    invoiceItemUnitCost = invoiceItemUnitCost,
    invoiceItemQuantity = invoiceItemQuantity,
    itemDiscountType = itemDiscountType,
    itemDiscountAmount = itemDiscountAmount,
    invoiceItemTaxable = invoiceItemTaxable,
    invoiceItemDetails = invoiceItemDetails,
    itemTotal = itemTotal,
    itemTotalDiscount = itemTotalDiscount,
    itemTaxRate = itemTaxRate,
)

fun InvoiceItem.toEntity(): InvoiceItemEntity = InvoiceItemEntity(
    invoiceItemId = invoiceItemId,
    invoiceIdFK = invoiceIdFK,
    invoiceItemName = invoiceItemName,
    invoiceItemUnitCost = invoiceItemUnitCost,
    invoiceItemQuantity = invoiceItemQuantity,
    itemDiscountType = itemDiscountType,
    itemDiscountAmount = itemDiscountAmount,
    invoiceItemTaxable = invoiceItemTaxable,
    invoiceItemDetails = invoiceItemDetails,
    itemTotal = itemTotal,
    itemTotalDiscount = itemTotalDiscount,
    itemTaxRate = itemTaxRate,
)

fun List<InvoiceItemEntity>.toDomainItems(): List<InvoiceItem> = map { it.toDomain() }

fun InvoicePhotoEntity.toDomain(): InvoicePhoto = InvoicePhoto(
    invoicePhotoId = invoicePhotoId,
    invoiceIdFK = invoiceIdFK,
    invoicePhoto = invoicePhoto,
    invoicePhotoDescription = invoicePhotoDescription,
    invoicePhotoAdditionalDetails = invoicePhotoAdditionalDetails,
)

fun InvoicePhoto.toEntity(): InvoicePhotoEntity = InvoicePhotoEntity(
    invoicePhotoId = invoicePhotoId,
    invoiceIdFK = invoiceIdFK,
    invoicePhoto = invoicePhoto,
    invoicePhotoDescription = invoicePhotoDescription,
    invoicePhotoAdditionalDetails = invoicePhotoAdditionalDetails,
)

fun List<InvoicePhotoEntity>.toDomainPhotos(): List<InvoicePhoto> = map { it.toDomain() }