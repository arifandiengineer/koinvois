package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.EstimateItem

@Dao
interface EstimateItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimateItem(estimateItem: EstimateItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItems(estimateItems: List<EstimateItem>)

    @Query("SELECT * FROM estimateItem")
    suspend fun getAllEstimateItems(): List<EstimateItem>

    @Query("SELECT * FROM estimateItem WHERE estimate_id_fk = :estimateId")
    suspend fun getEstimateItemsByEstimateId(estimateId: Int): List<EstimateItem>

    @Update
    suspend fun updateEstimateItem(vararg estimateItem: EstimateItem)

    @Delete
    suspend fun deleteEstimateItem(vararg estimateItem: EstimateItem)

    @Query("DELETE FROM EstimateItem WHERE estimate_id_fk = :estimateId ")
    suspend fun deleteEstimateItems(estimateId: Long)
}