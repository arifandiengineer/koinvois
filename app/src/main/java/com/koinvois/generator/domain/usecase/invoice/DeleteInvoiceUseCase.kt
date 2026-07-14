package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

/**
 * Mirrors the original InvoiceMainViewModel.deleteInvoice(): deleting an
 * invoice also cascades to its line items and photos (no DB-level FK cascade).
 */
class DeleteInvoiceUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceId: Long) {
        repository.deleteInvoiceWithID(invoiceId)
        repository.deleteInvoiceItems(invoiceId)
        repository.deleteInvoicePhotos(invoiceId)
    }
}