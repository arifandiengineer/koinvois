package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Insert
    suspend fun insertClient(client: Client)

    @Query("SELECT * FROM client")
    suspend fun getAllClients(): List<Client>

    @Query("SELECT * FROM client")
    fun observeAllClients(): Flow<List<Client>>

    @Query("SELECT * FROM client WHERE clientId = :clientId")
    suspend fun getClientById(clientId: Int) : Client

    @Update
    suspend fun updateClient(vararg client: Client)

    @Delete
    suspend fun deleteClient(vararg client: Client)
}