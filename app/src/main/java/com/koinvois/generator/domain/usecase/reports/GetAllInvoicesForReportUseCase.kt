package com.koinvois.generator.domain.usecase.reports

import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetAllInvoicesForReportUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(): List<Invoice> = repository.getAllInvoices()
}
