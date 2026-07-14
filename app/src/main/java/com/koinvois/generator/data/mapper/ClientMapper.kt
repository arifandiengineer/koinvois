package com.koinvois.generator.data.mapper

import com.koinvois.generator.database.models.Client as ClientEntity
import com.koinvois.generator.domain.model.Client

fun ClientEntity.toDomain(): Client = Client(
    clientId = clientId,
    clientName = clientName,
    clientEmail = clientEmail,
    clientMobile = clientMobile,
    clientPhone = clientPhone,
    clientFax = clientFax,
    clientContact = clientContact,
    clientAddress1 = clientAddress1,
    clientAddress2 = clientAddress2,
    clientAddress3 = clientAddress3,
)

fun Client.toEntity(): ClientEntity = ClientEntity(
    clientId = clientId,
    clientName = clientName,
    clientEmail = clientEmail,
    clientMobile = clientMobile,
    clientPhone = clientPhone,
    clientFax = clientFax,
    clientContact = clientContact,
    clientAddress1 = clientAddress1,
    clientAddress2 = clientAddress2,
    clientAddress3 = clientAddress3,
)

fun List<ClientEntity>.toDomain(): List<Client> = map { it.toDomain() }