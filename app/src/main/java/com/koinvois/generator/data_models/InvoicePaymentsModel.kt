package com.koinvois.generator.data_models

data class InvoicePaymentsModel(
    val amount: Float?,
    val paymentMethod: String?,
    val paymentDate: String?,
    val paymentNotes: String?
)