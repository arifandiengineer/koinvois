package com.koinvois.generator.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.database.daos.PersonalBusinessDao
import com.koinvois.generator.database.models.PersonalBusiness
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonalBusinessDaoTest {
    private lateinit var db: AppDataBase
    private lateinit var dao: PersonalBusinessDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        dao = db.personalBusinessDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetBusiness() = runBlocking {
        val business = PersonalBusiness(
            Id = 1,
            businessLogo = null,
            businessName = "Test Business",
            businessOwnerName = "Owner",
            businessNumber = "123",
            businessAddress1 = "Address 1",
            businessAddress2 = null,
            businessAddress3 = null,
            businessEmail = "test@example.com",
            businessPhoneNumber = 12345,
            businessMobileNumber = 67890,
            businessWebsite = "www.test.com"
        )
        dao.insertBusiness(business)
        val loaded = dao.getBusinessById(1)
        assertEquals("Test Business", loaded.businessName)
    }

    @Test
    fun updateBusiness() = runBlocking {
        val business = PersonalBusiness(1, null, "Name", null, null, null, null, null, null, null, null, null)
        dao.insertBusiness(business)
        
        val updated = PersonalBusiness(1, null, "Updated Name", null, null, null, null, null, null, null, null, null)
        dao.updateBusiness(updated)
        
        val loaded = dao.getBusinessById(1)
        assertEquals("Updated Name", loaded.businessName)
    }
}
