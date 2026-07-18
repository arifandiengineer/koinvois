package com.koinvois.generator.ui.invoices

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.core.utils.DateFormatter
import com.koinvois.generator.database.models.Invoice
import com.koinvois.generator.domain.usecase.business.AddBusinessUseCase
import com.koinvois.generator.domain.usecase.business.GetBusinessUseCase
import com.koinvois.generator.domain.usecase.business.UpdateBusinessUseCase
import com.koinvois.generator.domain.usecase.client.GetAllClientsUseCase
import com.koinvois.generator.domain.usecase.invoice.*
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
class InvoiceMainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observeAllInvoicesUseCase = mockk<ObserveAllInvoicesUseCase>()
    private val getInvoiceByIdUseCase = mockk<GetInvoiceByIdUseCase>()
    private val addInvoiceUseCase = mockk<AddInvoiceUseCase>()
    private val updateInvoiceUseCase = mockk<UpdateInvoiceUseCase>()
    private val deleteInvoiceUseCase = mockk<DeleteInvoiceUseCase>()
    private val getInvoiceItemsByInvoiceIdUseCase = mockk<GetInvoiceItemsByInvoiceIdUseCase>()
    private val addInvoiceItemUseCase = mockk<AddInvoiceItemUseCase>()
    private val updateInvoiceItemUseCase = mockk<UpdateInvoiceItemUseCase>()
    private val deleteInvoiceItemUseCase = mockk<DeleteInvoiceItemUseCase>()
    private val getInvoicePhotosByInvoiceIdUseCase = mockk<GetInvoicePhotosByInvoiceIdUseCase>()
    private val addInvoicePhotoUseCase = mockk<AddInvoicePhotoUseCase>()
    private val updateInvoicePhotoUseCase = mockk<UpdateInvoicePhotoUseCase>()
    private val deleteInvoicePhotoUseCase = mockk<DeleteInvoicePhotoUseCase>()
    private val getBusinessUseCase = mockk<GetBusinessUseCase>()
    private val addBusinessUseCase = mockk<AddBusinessUseCase>()
    private val updateBusinessUseCase = mockk<UpdateBusinessUseCase>()
    private val getAllClientsUseCase = mockk<GetAllClientsUseCase>()
    private val getAllItemsUseCase = mockk<GetAllItemsUseCase>()
    private val appPreferences = mockk<AppPreferencesDataStore>()
    private val dateFormatter = mockk<DateFormatter>()
    private val draft = InvoiceDraftState()

    private lateinit var viewModel: InvoiceMainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        coEvery { getAllClientsUseCase() } returns emptyList()
        coEvery { getAllItemsUseCase() } returns emptyList()
        coEvery { observeAllInvoicesUseCase() } returns flowOf(emptyList())

        viewModel = InvoiceMainViewModel(
            observeAllInvoicesUseCase, getInvoiceByIdUseCase, addInvoiceUseCase,
            updateInvoiceUseCase, deleteInvoiceUseCase, getInvoiceItemsByInvoiceIdUseCase,
            addInvoiceItemUseCase, updateInvoiceItemUseCase, deleteInvoiceItemUseCase,
            getInvoicePhotosByInvoiceIdUseCase, addInvoicePhotoUseCase, updateInvoicePhotoUseCase,
            deleteInvoicePhotoUseCase, getBusinessUseCase, addBusinessUseCase,
            updateBusinessUseCase, getAllClientsUseCase, getAllItemsUseCase,
            appPreferences, dateFormatter, draft
        )
    }

    @Test
    fun `prepareNewInvoice - sets default values`() = runTest {
        coEvery { appPreferences.getInvoiceNumber() } returns flowOf(10)
        coEvery { dateFormatter.today() } returns "2023-10-27"
        coEvery { getBusinessUseCase(1) } returns mockk(relaxed = true)
        coEvery { addInvoiceUseCase(any()) } returns Unit

        viewModel.prepareNewInvoice()

        assertEquals(10, viewModel.invoiceNumber)
        assertEquals("2023-10-27", viewModel.invoiceDate)
        assertEquals("unpaid", viewModel.invoiceStatus)
    }
}
