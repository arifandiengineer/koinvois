package com.koinvois.generator.ui.client

import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.domain.usecase.client.AddClientUseCase
import com.koinvois.generator.domain.usecase.client.DeleteClientUseCase
import com.koinvois.generator.domain.usecase.client.GetAllClientsUseCase
import com.koinvois.generator.domain.usecase.client.UpdateClientUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClientMainViewModelTest {

    private val getAllClientsUseCase = mockk<GetAllClientsUseCase>()
    private val addClientUseCase = mockk<AddClientUseCase>()
    private val updateClientUseCase = mockk<UpdateClientUseCase>()
    private val deleteClientUseCase = mockk<DeleteClientUseCase>()
    private lateinit var viewModel: ClientMainViewModel

    @Before
    fun setup() {
        viewModel = ClientMainViewModel(getAllClientsUseCase, addClientUseCase, updateClientUseCase, deleteClientUseCase)
    }

    @Test
    fun `getClientById - returns correct client`() = runTest {
        val clients = listOf(
            Client(1, "Alice", null, null, null, null, null, null, null, null),
            Client(2, "Bob", null, null, null, null, null, null, null, null)
        )
        coEvery { getAllClientsUseCase() } returns clients

        val result = viewModel.getClientById(2)
        assertEquals("Bob", result?.clientName)
    }
}
