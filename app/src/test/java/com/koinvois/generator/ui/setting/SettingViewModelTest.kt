package com.koinvois.generator.ui.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.usecase.business.AddBusinessUseCase
import com.koinvois.generator.domain.usecase.business.DeleteBusinessUseCase
import com.koinvois.generator.domain.usecase.business.GetBusinessUseCase
import com.koinvois.generator.domain.usecase.business.UpdateBusinessUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val addUseCase = mockk<AddBusinessUseCase>()
    private val updateUseCase = mockk<UpdateBusinessUseCase>()
    private val deleteUseCase = mockk<DeleteBusinessUseCase>()
    private val getUseCase = mockk<GetBusinessUseCase>()
    private lateinit var viewModel: SettingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingViewModel(addUseCase, updateUseCase, deleteUseCase, getUseCase)
    }

    @Test
    fun `getBusiness - updates LiveData`() = runTest {
        val business = mockk<PersonalBusiness>()
        coEvery { getUseCase(1) } returns business

        viewModel.getBusiness()
        advanceUntilIdle()

        assertEquals(business, viewModel.businessUpdateModel.value)
    }
}
