package com.koinvois.generator.domain.usecase.invoice

import com.koinvois.generator.domain.model.InvoiceItem
import com.koinvois.generator.domain.repository.InvoiceRepository
import javax.inject.Inject

/**
 * The original InvoiceMainViewModel.updateInvoiceItem() called insert, not
 * update, on the DAO. Preserved as-is: InvoiceItemDao's insert uses
 * OnConflictStrategy.REPLACE, so it behaves as an upsert on the same primary
 * key - functionally equivalent, not a bug to fix here.
 */
class UpdateInvoiceItemUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceItem: InvoiceItem) = repository.insertInvoiceItem(invoiceItem)
}