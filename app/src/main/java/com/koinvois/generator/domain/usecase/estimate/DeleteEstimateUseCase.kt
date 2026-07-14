package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * Mirrors the original EstimatesMainViewModel.deleteEstimate(): deleting an
 * estimate also cascades to its line items and photos (no DB-level FK cascade).
 */
class DeleteEstimateUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimateId: Long) {
        repository.deleteEstimateWithID(estimateId)
        repository.deleteEstimateItems(estimateId)
        repository.deleteEstimatePhotos(estimateId)
    }
}