package com.koinvois.generator.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.usecase.dashboard.DashboardSummary
import com.koinvois.generator.domain.usecase.dashboard.GetDashboardSummaryUseCase
import com.koinvois.generator.domain.usecase.invoice.ObserveAllInvoicesUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getDashboardSummaryUseCase = mockk<GetDashboardSummaryUseCase>()
    private val observeAllInvoicesUseCase = mockk<ObserveAllInvoicesUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val mockSummary = DashboardSummary(
            totalInvoices = 10,
            totalInvoicesTrend = "+10%",
            totalRevenuePaid = 1000f,
            revenueTrend = "+5%",
            totalOutstandingAmount = 500f,
            outstandingTrend = "-2%",
            openEstimateCount = 2,
            estimatesTrend = "+1"
        )
        every { getDashboardSummaryUseCase() } returns flowOf(mockSummary)
    }

    @Test
    fun `recentInvoices - takes only 5 latest invoices`() = runTest {
        val invoices = (1..10).map { id ->
            mockk<Invoice> {
                every { invoiceId } returns id
            }
        }
        every { observeAllInvoicesUseCase() } returns flowOf(invoices)

        val viewModel = DashboardViewModel(getDashboardSummaryUseCase, observeAllInvoicesUseCase)
        advanceUntilIdle()

        val recent = viewModel.recentInvoices.value
        assertEquals(5, recent?.size)
        assertEquals(10, recent?.first()?.invoiceId)
        assertEquals(6, recent?.last()?.invoiceId)
    }
}
