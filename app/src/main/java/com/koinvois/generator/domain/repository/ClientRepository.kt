package com.koinvois.generator.domain.repository

import com.koinvois.generator.domain.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun observeAllClients(): Flow<List<Client>>
    suspend fun getAllClients(): List<Client>
    suspend fun getClientById(clientId: Int): Client
    suspend fun insertClient(client: Client)
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(client: Client)
}