package com.koinvois.generator.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.koinvois.generator.database.models.Estimate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstimateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimate(estimate: Estimate)

    @Query("SELECT * FROM estimate")
    suspend fun getAllEstimates(): List<Estimate>

    @Query("SELECT * FROM estimate")
    fun observeAllEstimates(): Flow<List<Estimate>>

    @Query("SELECT * FROM estimate")
    fun getAllEstimatesLive(): LiveData<List<Estimate>>

    @Query("SELECT * FROM estimate WHERE estimateId = :estimateId")
    suspend fun getEstimateById(estimateId: Int): Estimate

    @Update
    suspend fun updateEstimate(vararg Estimate: Estimate)

    @Delete
    suspend fun deleteEstimate(vararg estimate: Estimate)

    @Query("Delete from estimate where estimateId = :id")
    suspend fun deleteEstimateWithID(vararg id: Long)
}