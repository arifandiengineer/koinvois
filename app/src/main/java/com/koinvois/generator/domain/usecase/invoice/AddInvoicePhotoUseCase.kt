package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.InvoicePhoto
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

class AddInvoicePhotoUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoicePhoto: InvoicePhoto) = repository.insertInvoicePhoto(invoicePhoto)
}