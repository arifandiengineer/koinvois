package com.koinvois.generator.domain.usecase.dashboard

import com.koinvois.generator.domain.repository.EstimateRepository
import com.koinvois.generator.domain.repository.InvoiceRepository
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class DashboardSummary(
    val totalInvoices: Int,
    val totalInvoicesTrend: String,
    val totalRevenuePaid: Float,
    val revenueTrend: String,
    val totalOutstandingAmount: Float,
    val outstandingTrend: String,
    val openEstimateCount: Int,
    val estimatesTrend: String,
)

class GetDashboardSummaryUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val estimateRepository: EstimateRepository,
) {
    private val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    operator fun invoke(): Flow<DashboardSummary> =
        combine(
            invoiceRepository.observeAllInvoices(),
            estimateRepository.observeAllEstimates()
        ) { invoices, estimates ->
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)
            
            calendar.add(Calendar.MONTH, -1)
            val lastMonth = calendar.get(Calendar.MONTH)
            val lastYear = calendar.get(Calendar.YEAR)

            val currentMonthInvoices = invoices.filter { isSameMonth(it.invoiceDate, currentMonth, currentYear) }
            val lastMonthInvoices = invoices.filter { isSameMonth(it.invoiceDate, lastMonth, lastYear) }

            val paid = invoices.filter { it.invoiceStatus == InvoiceStatusEnum.PAID.status }
            val outstanding = invoices.filter { it.invoiceStatus == InvoiceStatusEnum.UN_PAID.status }
            
            val currentPaid = currentMonthInvoices.filter { it.invoiceStatus == InvoiceStatusEnum.PAID.status }.sumOf { it.invoiceTotal?.toDouble() ?: 0.0 }
            val lastPaid = lastMonthInvoices.filter { it.invoiceStatus == InvoiceStatusEnum.PAID.status }.sumOf { it.invoiceTotal?.toDouble() ?: 0.0 }
            
            val currentOutstanding = currentMonthInvoices.filter { it.invoiceStatus == InvoiceStatusEnum.UN_PAID.status }.sumOf { it.invoiceTotal?.toDouble() ?: 0.0 }
            val lastOutstanding = lastMonthInvoices.filter { it.invoiceStatus == InvoiceStatusEnum.UN_PAID.status }.sumOf { it.invoiceTotal?.toDouble() ?: 0.0 }

            DashboardSummary(
                totalInvoices = invoices.size,
                totalInvoicesTrend = calculateTrend(currentMonthInvoices.size, lastMonthInvoices.size),
                totalRevenuePaid = paid.sumOf { (it.invoiceTotal ?: 0f).toDouble() }.toFloat(),
                revenueTrend = calculateTrend(currentPaid, lastPaid),
                totalOutstandingAmount = outstanding.sumOf { (it.invoiceTotal ?: 0f).toDouble() }.toFloat(),
                outstandingTrend = calculateTrend(currentOutstanding, lastOutstanding),
                openEstimateCount = estimates.count { it.estimateStatus == EstimateStatusEnum.OPEN.status },
                estimatesTrend = "+${estimates.count { it.estimateStatus == EstimateStatusEnum.OPEN.status && isSameMonth(it.estimateDate, currentMonth, currentYear) }} from last month"
            )
        }

    private fun isSameMonth(dateStr: String?, month: Int, year: Int): Boolean {
        if (dateStr == null) return false
        return try {
            val date = sdf.parse(dateStr)
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            } else false
        } catch (e: Exception) {
            false
        }
    }

    private fun calculateTrend(current: Number, last: Number): String {
        val c = current.toDouble()
        val l = last.toDouble()
        if (l == 0.0) return "+0% from last month"
        val diff = ((c - l) / l) * 100
        val prefix = if (diff >= 0) "+" else ""
        return "${prefix}${String.format(Locale.getDefault(), "%.0f", diff)}% from last month"
    }
}
