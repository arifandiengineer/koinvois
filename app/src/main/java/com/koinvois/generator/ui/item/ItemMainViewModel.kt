package com.koinvois.generator.ui.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.domain.usecase.item.AddItemUseCase
import com.koinvois.generator.domain.usecase.item.DeleteItemUseCase
import com.koinvois.generator.domain.usecase.item.GetAllItemsUseCase
import com.koinvois.generator.domain.usecase.item.UpdateItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemMainViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {
    var itemUpdateModel: MutableLiveData<Item?> = MutableLiveData()

    suspend fun getAllItems(): ArrayList<Item> {
        return ArrayList(getAllItemsUseCase())
    }

    suspend fun addItem(item: Item) {
        addItemUseCase(item)
    }

    suspend fun updateItem(item: Item) {
        updateItemUseCase(item)
    }

    suspend fun deleteItem() {
        itemUpdateModel.value?.let { deleteItemUseCase(it) }
    }
}