package com.koinvois.generator.ui.splash

import androidx.lifecycle.ViewModel
import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.usecase.business.SaveBusinessOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashMainViewModel @Inject constructor(
    private val saveBusinessOnboardingUseCase: SaveBusinessOnboardingUseCase
) : ViewModel() {

    suspend fun addBusiness(business: PersonalBusiness) {
        saveBusinessOnboardingUseCase(business)
    }
}