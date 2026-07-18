package com.koinvois.generator.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.database.daos.EstimateDao
import com.koinvois.generator.database.models.Estimate
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstimateDaoTest {
    private lateinit var db: AppDataBase
    private lateinit var dao: EstimateDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        dao = db.estimateDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetEstimate() = runBlocking {
        val estimate = Estimate(
            estimateId = 1,
            estimateNumber = 500,
            estimateDate = "2023-10-27",
            estimateStatus = EstimateStatusEnum.OPEN.status
        )
        dao.insertEstimate(estimate)
        val loaded = dao.getEstimateById(1)
        assertEquals(500, loaded.estimateNumber)
    }
}
