package com.koinvois.generator.ui.splash

import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.usecase.business.SaveBusinessOnboardingUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashMainViewModelTest {

    private val saveBusinessOnboardingUseCase = mockk<SaveBusinessOnboardingUseCase>()
    private lateinit var viewModel: SplashMainViewModel

    @Before
    fun setup() {
        viewModel = SplashMainViewModel(saveBusinessOnboardingUseCase)
    }

    @Test
    fun `addBusiness - calls use case`() = runTest {
        val business = mockk<PersonalBusiness>()
        coEvery { saveBusinessOnboardingUseCase(business) } returns Unit

        viewModel.addBusiness(business)

        coVerify { saveBusinessOnboardingUseCase(business) }
    }
}
