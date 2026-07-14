package com.koinvois.generator.domain.usecase.client

import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.domain.repository.ClientRepository
import javax.inject.Inject

class GetAllClientsUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(): List<Client> = repository.getAllClients()
}