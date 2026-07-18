package com.koinvois.generator.ui.reports

import android.content.Context
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.usecase.reports.GetAllInvoicesForReportUseCase
import com.koinvois.generator.domain.usecase.reports.GetAllPaidInvoicesForReportUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsMainViewModelTest {

    private val context = mockk<Context>(relaxed = true)
    private val getAllInvoicesUseCase = mockk<GetAllInvoicesForReportUseCase>()
    private val getAllPaidInvoicesUseCase = mockk<GetAllPaidInvoicesForReportUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { context.getString(any()) } returns "No Client"
    }

    @Test
    fun `loadReports - groups invoices by client and sums amount`() = runTest {
        val invoices = listOf(
            createMockInvoice("Client A", 100f),
            createMockInvoice("Client A", 50f),
            createMockInvoice("Client B", 200f)
        )
        coEvery { getAllInvoicesUseCase() } returns invoices
        coEvery { getAllPaidInvoicesUseCase() } returns emptyList()

        val viewModel = ReportsMainViewModel(context, getAllInvoicesUseCase, getAllPaidInvoicesUseCase, testDispatcher)
        advanceUntilIdle()

        val report = viewModel.clientsReport.value
        assertEquals(2, report.size)
        
        val clientA = report.find { it.client == "Client A" }
        assertEquals(2, clientA?.invoicesCount)
        assertEquals(150f, clientA?.invoiceAmount)

        val clientB = report.find { it.client == "Client B" }
        assertEquals(1, clientB?.invoicesCount)
        assertEquals(200f, clientB?.invoiceAmount)
    }

    private fun createMockInvoice(clientName: String, total: Float) = Invoice(
        invoiceId = 0,
        invoiceNumber = 1,
        invoiceDate = "date",
        invoiceTerms = null,
        invoiceDueDate = null,
        invoicePoNumber = null,
        invoiceSubtotal = total,
        invoiceDiscountType = null,
        invoiceDiscountAmount = null,
        discountTotalAmount = null,
        invoiceTaxType = null,
        invoiceTaxLabel = null,
        invoiceTaxRate = null,
        invoiceTaxInclusive = null,
        invoiceTotal = total,
        invoicePaymentInstruction = null,
        invoiceSignature = null,
        signatureDate = null,
        invoiceNotes = null,
        invoiceStatus = "UNPAID",
        clientPK = 1,
        invoiceClientName = clientName,
        invoiceClientEmail = null,
        invoiceClientMobile = null,
        invoiceClientPhone = null,
        invoiceClientFax = null,
        invoiceClientContact = null,
        invoiceClientAddress1 = null,
        invoiceClientAddress2 = null,
        invoiceClientAddress3 = null
    )
}
