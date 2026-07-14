package com.koinvois.generator.domain.model

data class Client(
    val clientId: Int,
    val clientName: String?,
    val clientEmail: String?,
    val clientMobile: Int?,
    val clientPhone: Int?,
    val clientFax: Int?,
    val clientContact: String?,
    val clientAddress1: String?,
    val clientAddress2: String?,
    val clientAddress3: String?,
)
