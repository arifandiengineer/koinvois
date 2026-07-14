package com.koinvois.generator.domain.usecase.business

import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.repository.PersonalBusinessRepository
import javax.inject.Inject

class DeleteBusinessUseCase @Inject constructor(
    private val repository: PersonalBusinessRepository
) {
    suspend operator fun invoke(business: PersonalBusiness) =
        repository.deleteBusiness(business)
}
