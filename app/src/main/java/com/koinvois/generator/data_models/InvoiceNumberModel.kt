package com.koinvois.generator.data_models

data class InvoiceNumberModel(
    val invoiceNumber: Int?,
    val invoiceDate: String?,
    val invoiceTerms: String?,
    val invoiceDueDate: String?,
    val invoicePoNumber: String?
)
