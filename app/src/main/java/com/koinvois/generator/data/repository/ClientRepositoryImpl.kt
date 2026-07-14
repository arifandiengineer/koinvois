package com.koinvois.generator.data.repository

import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.database.daos.ClientDao
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao
) : ClientRepository {

    override fun observeAllClients(): Flow<List<Client>> =
        clientDao.observeAllClients().map { it.toDomain() }

    override suspend fun getAllClients(): List<Client> =
        clientDao.getAllClients().toDomain()

    override suspend fun getClientById(clientId: Int): Client =
        clientDao.getClientById(clientId).toDomain()

    override suspend fun insertClient(client: Client) =
        clientDao.insertClient(client.toEntity())

    override suspend fun updateClient(client: Client) =
        clientDao.updateClient(client.toEntity())

    override suspend fun deleteClient(client: Client) =
        clientDao.deleteClient(client.toEntity())
}