package com.koinvois.generator.domain.usecase.estimate

import com.koinvois.generator.domain.model.EstimatePhoto
import com.koinvois.generator.domain.repository.EstimateRepository
import javax.inject.Inject

class DeleteEstimatePhotoUseCase @Inject constructor(
    private val repository: EstimateRepository
) {
    suspend operator fun invoke(estimatePhoto: EstimatePhoto) = repository.deleteEstimatePhoto(estimatePhoto)
}