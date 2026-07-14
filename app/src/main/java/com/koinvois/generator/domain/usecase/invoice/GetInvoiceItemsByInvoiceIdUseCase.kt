package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.InvoiceItem
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoiceItemsByInvoiceIdUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceId: Int): List<InvoiceItem> =
        repository.getInvoiceItemsByInvoiceId(invoiceId)
}