package com.koinvois.generator.ui.item

import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.domain.usecase.item.AddItemUseCase
import com.koinvois.generator.domain.usecase.item.DeleteItemUseCase
import com.koinvois.generator.domain.usecase.item.GetAllItemsUseCase
import com.koinvois.generator.domain.usecase.item.UpdateItemUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemMainViewModelTest {

    private val getAllItemsUseCase = mockk<GetAllItemsUseCase>()
    private val addItemUseCase = mockk<AddItemUseCase>()
    private val updateItemUseCase = mockk<UpdateItemUseCase>()
    private val deleteItemUseCase = mockk<DeleteItemUseCase>()
    private lateinit var viewModel: ItemMainViewModel

    @Before
    fun setup() {
        viewModel = ItemMainViewModel(getAllItemsUseCase, addItemUseCase, updateItemUseCase, deleteItemUseCase)
    }

    @Test
    fun `getAllItems - returns list from use case`() = runTest {
        val items = listOf(Item(1, "Item 1", 10f, false, null))
        coEvery { getAllItemsUseCase() } returns items

        val result = viewModel.getAllItems()
        assertEquals(1, result.size)
        assertEquals("Item 1", result[0].itemName)
    }

    @Test
    fun `addItem - calls use case`() = runTest {
        val item = Item(0, "New", 20f, true, null)
        coEvery { addItemUseCase(item) } returns Unit

        viewModel.addItem(item)
        coVerify { addItemUseCase(item) }
    }
}
