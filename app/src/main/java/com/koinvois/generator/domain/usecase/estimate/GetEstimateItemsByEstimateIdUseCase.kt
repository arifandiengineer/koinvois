package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.EstimateItem
import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

class GetEstimateItemsByEstimateIdUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimateId: Int): List<EstimateItem> =
        repository.getEstimateItemsByEstimateId(estimateId)
}