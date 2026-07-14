package com.koinvois.generator.data.repository

import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.database.daos.ItemDao
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao
) : ItemRepository {

    override fun observeAllItems(): Flow<List<Item>> =
        itemDao.observeAllItems().map { it.toDomain() }

    override suspend fun getAllItems(): List<Item> =
        itemDao.getAllItems().toDomain()

    override suspend fun getItemById(itemId: Int): Item =
        itemDao.getItemById(itemId).toDomain()

    override suspend fun insertItem(item: Item) =
        itemDao.insertItem(item.toEntity())

    override suspend fun updateItem(item: Item) =
        itemDao.updateItem(item.toEntity())

    override suspend fun deleteItem(item: Item) =
        itemDao.deleteItem(item.toEntity())
}