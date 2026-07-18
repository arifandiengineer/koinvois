package com.koinvois.generator.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.database.daos.ItemDao
import com.koinvois.generator.database.models.Item
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var db: AppDataBase
    private lateinit var dao: ItemDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        dao = db.itemDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetItem() = runBlocking {
        val item = Item(1, "Test Item", 100f, true, "Details")
        dao.insertItem(item)
        val loaded = dao.getAllItems()
        assertEquals(1, loaded.size)
        assertEquals("Test Item", loaded[0].itemName)
    }

    @Test
    fun deleteItem() = runBlocking {
        val item = Item(1, "Delete Me", 50f, false, null)
        dao.insertItem(item)
        dao.deleteItem(item)
        val loaded = dao.getAllItems()
        assertEquals(0, loaded.size)
    }
}
