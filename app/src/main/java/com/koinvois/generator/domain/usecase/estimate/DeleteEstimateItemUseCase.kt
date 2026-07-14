package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.EstimateItem
import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

class DeleteEstimateItemUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimateItem: EstimateItem) = repository.deleteEstimateItem(estimateItem)
}