package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.EstimateItem
import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * The original EstimatesMainViewModel.updateEstimateItem() called insert, not
 * update, on the DAO. Preserved as-is: EstimateItemDao's insert uses
 * OnConflictStrategy.REPLACE, so it behaves as an upsert on the same primary
 * key - functionally equivalent, not a bug to fix here (same pattern as
 * Invoice's UpdateInvoiceItemUseCase from Sprint 6).
 */
class UpdateEstimateItemUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimateItem: EstimateItem) = repository.insertEstimateItem(estimateItem)
}