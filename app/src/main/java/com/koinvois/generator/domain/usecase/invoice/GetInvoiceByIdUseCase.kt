package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoiceByIdUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceId: Int): Invoice {
        return repository.getInvoiceById(invoiceId)
    }
}
