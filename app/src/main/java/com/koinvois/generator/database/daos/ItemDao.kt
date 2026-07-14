package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert
    suspend fun insertItem(item: Item)

    @Query("SELECT * FROM item")
    suspend fun getAllItems(): List<Item>

    @Query("SELECT * FROM item")
    fun observeAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE itemId = :itemId")
    suspend fun getItemById(itemId: Int): Item

    @Update
    suspend fun updateItem(vararg item: Item)

    @Delete
    suspend fun deleteItem(vararg item: Item)
}