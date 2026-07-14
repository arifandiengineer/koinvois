package com.koinvois.generator.ui.client

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koinvois.generator.domain.model.Client
import com.koinvois.generator.domain.usecase.client.AddClientUseCase
import com.koinvois.generator.domain.usecase.client.DeleteClientUseCase
import com.koinvois.generator.domain.usecase.client.GetAllClientsUseCase
import com.koinvois.generator.domain.usecase.client.UpdateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientMainViewModel @Inject constructor(
    private val getAllClientsUseCase: GetAllClientsUseCase,
    private val addClientUseCase: AddClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase
) : ViewModel() {

    var allClientList: MutableLiveData<ArrayList<Client>> = MutableLiveData()
    var clientUpdateModel: MutableLiveData<Client?> = MutableLiveData()

    suspend fun getAllClients(): ArrayList<Client> {
        return ArrayList(getAllClientsUseCase())
    }

    suspend fun addClient(client: Client) {
        addClientUseCase(client)
    }

    suspend fun updateClient(client: Client) {
        updateClientUseCase(client)
    }

    suspend fun deleteClient() {
        clientUpdateModel.value?.let { deleteClientUseCase(it) }
    }
}