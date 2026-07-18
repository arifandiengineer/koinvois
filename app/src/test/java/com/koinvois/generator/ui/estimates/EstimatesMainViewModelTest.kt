package com.koinvois.generator.ui.estimates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.domain.usecase.business.GetBusinessUseCase
import com.koinvois.generator.domain.usecase.client.GetAllClientsUseCase
import com.koinvois.generator.domain.usecase.estimate.*
import com.koinvois.generator.domain.usecase.item.GetAllItemsUseCase
import android.util.Log
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EstimatesMainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observeAllEstimatesUseCase = mockk<ObserveAllEstimatesUseCase>()
    private val getEstimateByIdUseCase = mockk<GetEstimateByIdUseCase>()
    private val addEstimateUseCase = mockk<AddEstimateUseCase>()
    private val updateEstimateUseCase = mockk<UpdateEstimateUseCase>()
    private val deleteEstimateUseCase = mockk<DeleteEstimateUseCase>()
    private val getEstimateItemsByEstimateIdUseCase = mockk<GetEstimateItemsByEstimateIdUseCase>()
    private val addEstimateItemUseCase = mockk<AddEstimateItemUseCase>()
    private val updateEstimateItemUseCase = mockk<UpdateEstimateItemUseCase>()
    private val deleteEstimateItemUseCase = mockk<DeleteEstimateItemUseCase>()
    private val getEstimatePhotosByEstimateIdUseCase = mockk<GetEstimatePhotosByEstimateIdUseCase>()
    private val addEstimatePhotoUseCase = mockk<AddEstimatePhotoUseCase>()
    private val updateEstimatePhotoUseCase = mockk<UpdateEstimatePhotoUseCase>()
    private val deleteEstimatePhotoUseCase = mockk<DeleteEstimatePhotoUseCase>()
    private val getBusinessUseCase = mockk<GetBusinessUseCase>()
    private val getAllClientsUseCase = mockk<GetAllClientsUseCase>()
    private val getAllItemsUseCase = mockk<GetAllItemsUseCase>()
    private val appPreferences = mockk<AppPreferencesDataStore>()
    private val draft = EstimateDraftState()

    private lateinit var viewModel: EstimatesMainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        coEvery { getAllClientsUseCase() } returns emptyList()
        coEvery { getAllItemsUseCase() } returns emptyList()
        coEvery { observeAllEstimatesUseCase() } returns flowOf(emptyList())

        viewModel = EstimatesMainViewModel(
            observeAllEstimatesUseCase, getEstimateByIdUseCase, addEstimateUseCase,
            updateEstimateUseCase, deleteEstimateUseCase, getEstimateItemsByEstimateIdUseCase,
            addEstimateItemUseCase, updateEstimateItemUseCase, deleteEstimateItemUseCase,
            getEstimatePhotosByEstimateIdUseCase, addEstimatePhotoUseCase, updateEstimatePhotoUseCase,
            deleteEstimatePhotoUseCase, getBusinessUseCase, mockk(), mockk(),
            getAllClientsUseCase, getAllItemsUseCase, appPreferences, draft
        )
    }

    @Test
    fun `prepareNewEstimate - sets default values`() = runTest {
        coEvery { appPreferences.getEstimateNumber() } returns flowOf(50)
        coEvery { addEstimateUseCase(any()) } returns Unit

        viewModel.prepareNewEstimate()

        assertEquals(50, viewModel.estimateNumber)
        assertEquals("open", viewModel.estimateStatus)
    }
}
