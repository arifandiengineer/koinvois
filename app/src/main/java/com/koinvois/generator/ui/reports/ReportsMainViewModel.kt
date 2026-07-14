package com.koinvois.generator.ui.reports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koinvois.generator.R
import com.koinvois.generator.domain.model.Invoice
import com.koinvois.generator.domain.usecase.reports.GetAllInvoicesForReportUseCase
import com.koinvois.generator.domain.usecase.reports.GetAllPaidInvoicesForReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsMainViewModel @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    private val getAllInvoicesForReportUseCase: GetAllInvoicesForReportUseCase,
    private val getAllPaidInvoicesForReportUseCase: GetAllPaidInvoicesForReportUseCase
) : ViewModel() {

    val clientsReportList: ArrayList<ClientReportModel> = arrayListOf()
    val paidInvoicesLive: MutableLiveData<List<Invoice>> = MutableLiveData()
    val clientReportLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val paidClientReportLoaded: MutableLiveData<Boolean> = MutableLiveData()


    fun getClientReport() {
        viewModelScope.launch(Dispatchers.Default) {
            getAllInvoicesForReportUseCase().forEach {
                clientsReportList.add(
                    ClientReportModel(
                        it.invoiceClientName ?: context.getString(R.string.fallback_no_client),
                        1,
                        it.invoiceTotal ?: 0f
                    )
                )
            }

            clientReportLoaded.postValue(true)
        }
    }

    fun getPaidInvoicesReport() {
        viewModelScope.launch(Dispatchers.Default) {
            val list = getAllPaidInvoicesForReportUseCase()
            paidInvoicesLive.postValue(list)
            paidClientReportLoaded.postValue(true)
        }
    }

    data class ClientReportModel(
        val client: String,
        val invoicesCount: Int,
        val invoiceAmount: Float
    )
}