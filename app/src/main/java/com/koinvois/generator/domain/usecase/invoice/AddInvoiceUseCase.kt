package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

class AddInvoiceUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoice: Invoice) = repository.insertInvoice(invoice)
}