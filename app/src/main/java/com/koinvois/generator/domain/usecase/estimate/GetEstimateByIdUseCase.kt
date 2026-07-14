package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.Estimate
import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

class GetEstimateByIdUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimateId: Int): Estimate = repository.getEstimateById(estimateId)
}
