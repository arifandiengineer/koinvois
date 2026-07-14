package com.koinvois.generator.domain.usecase.item

import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.domain.repository.ItemRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(item: Item) = repository.deleteItem(item)
}