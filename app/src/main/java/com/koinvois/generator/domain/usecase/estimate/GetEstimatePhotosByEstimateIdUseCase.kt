package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.EstimatePhoto
import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

class GetEstimatePhotosByEstimateIdUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimateId: Int): List<EstimatePhoto> =
        repository.getEstimatePhotosByEstimateId(estimateId)
}