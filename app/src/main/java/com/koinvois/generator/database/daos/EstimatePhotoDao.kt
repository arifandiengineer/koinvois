package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.EstimatePhoto

@Dao
interface EstimatePhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimatePhoto(estimatePhoto: EstimatePhoto)

    @Insert
    suspend fun insertEstimatePhotoList(estimatePhotoList: List<EstimatePhoto>)

    @Query("SELECT * FROM estimatePhoto")
    suspend fun getAllEstimatePhotos(): List<EstimatePhoto>

    @Query("SELECT * FROM EstimatePhoto WHERE estimate_id_fk = :estimateId")
    suspend fun getEstimatePhotosByEstimateId(estimateId: Int): List<EstimatePhoto>

    @Update
    suspend fun updateEstimatePhoto(vararg estimatePhoto: EstimatePhoto)

    @Delete
    suspend fun deleteEstimatePhoto(vararg estimatePhoto: EstimatePhoto)

    @Query("DELETE FROM EstimatePhoto WHERE estimate_id_fk = :estimateId")
    suspend fun deleteEstimatePhotos(estimateId: Long)
}