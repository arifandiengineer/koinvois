package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllInvoicesUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    operator fun invoke(): Flow<List<Invoice>> = repository.observeAllInvoices()
}