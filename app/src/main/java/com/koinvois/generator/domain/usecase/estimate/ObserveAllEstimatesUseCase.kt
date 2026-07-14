package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.Estimate
import com.koinvois.generator.domain.repository.EstimateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllEstimatesUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    operator fun invoke(): Flow<List<Estimate>> = repository.observeAllEstimates()
}