package com.koinvois.generator.domain.repository

import com.koinvois.generator.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun observeAllItems(): Flow<List<Item>>
    suspend fun getAllItems(): List<Item>
    suspend fun getItemById(itemId: Int): Item
    suspend fun insertItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun deleteItem(item: Item)
}