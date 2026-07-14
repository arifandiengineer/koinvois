package com.koinvois.generator.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.usecase.dashboard.DashboardSummary
import com.koinvois.generator.domain.usecase.dashboard.GetDashboardSummaryUseCase
import com.koinvois.generator.domain.usecase.invoice.ObserveAllInvoicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RECENT_INVOICE_COUNT = 5

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    observeAllInvoicesUseCase: ObserveAllInvoicesUseCase,
) : ViewModel() {

    val summary: LiveData<DashboardSummary> = getDashboardSummaryUseCase().asLiveData()

    private val _recentInvoices = MutableLiveData<List<Invoice>>()
    val recentInvoices: LiveData<List<Invoice>> = _recentInvoices

    init {
        viewModelScope.launch {
            observeAllInvoicesUseCase().collect { invoices ->
                _recentInvoices.value =
                    invoices.sortedByDescending { it.invoiceId }.take(RECENT_INVOICE_COUNT)
            }
        }
    }
}
