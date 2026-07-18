package com.koinvois.generator.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koinvois.generator.R
import com.koinvois.generator.core.di.DefaultDispatcher
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.usecase.reports.GetAllInvoicesForReportUseCase
import com.koinvois.generator.domain.usecase.reports.GetAllPaidInvoicesForReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsMainViewModel @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    private val getAllInvoicesForReportUseCase: GetAllInvoicesForReportUseCase,
    private val getAllPaidInvoicesForReportUseCase: GetAllPaidInvoicesForReportUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _paidInvoices = MutableStateFlow<List<Invoice>>(emptyList())
    val paidInvoices: StateFlow<List<Invoice>> = _paidInvoices

    private val _clientsReport = MutableStateFlow<List<ClientReportModel>>(emptyList())
    val clientsReport: StateFlow<List<ClientReportModel>> = _clientsReport

    init {
        loadReports()
    }

    fun loadReports() {
        viewModelScope.launch(defaultDispatcher) {
            
            val paidList = getAllPaidInvoicesForReportUseCase()
            _paidInvoices.value = paidList

            
            val allInvoices = getAllInvoicesForReportUseCase()
            val groupedByClient = allInvoices.groupBy { 
                it.invoiceClientName ?: context.getString(R.string.fallback_no_client) 
            }

            val clientReportList = groupedByClient.map { (clientName, invoices) ->
                ClientReportModel(
                    client = clientName,
                    invoicesCount = invoices.size,
                    invoiceAmount = invoices.sumOf { (it.invoiceTotal ?: 0f).toDouble() }.toFloat()
                )
            }.sortedByDescending { it.invoiceAmount }

            _clientsReport.value = clientReportList
        }
    }

    data class ClientReportModel(
        val client: String,
        val invoicesCount: Int,
        val invoiceAmount: Float
    )
}