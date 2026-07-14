package com.koinvois.generator.domain.usecase.client

import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.domain.repository.ClientRepository
import javax.inject.Inject

class UpdateClientUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(client: Client) = repository.updateClient(client)
}
