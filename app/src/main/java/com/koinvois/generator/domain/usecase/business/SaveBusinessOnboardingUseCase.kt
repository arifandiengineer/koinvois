package com.koinvois.generator.domain.usecase.business

import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.repository.PersonalBusinessRepository
import javax.inject.Inject

/**
 * Onboarding upsert: the splash "create business" screen doesn't know if a
 * business row already exists, so it looks one up by name and inserts or
 * updates accordingly (mirrors the original SplashMainViewModel logic).
 */
class SaveBusinessOnboardingUseCase @Inject constructor(
    private val repository: PersonalBusinessRepository
) {
    suspend operator fun invoke(business: PersonalBusiness) {
        val existingBusiness = repository.getBusinessByName(business.businessName.toString())
        if (existingBusiness == null) {
            repository.insertBusiness(business)
        } else {
            repository.updateBusiness(business)
        }
    }
}