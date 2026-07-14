package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.InvoiceItem
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

class AddInvoiceItemUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceItem: InvoiceItem) = repository.insertInvoiceItem(invoiceItem)
}