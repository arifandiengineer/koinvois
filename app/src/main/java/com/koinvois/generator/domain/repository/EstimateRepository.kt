package com.koinvois.generator.domain.repository

import com.koinvois.generator.domain.model.Estimate
import com.koinvois.generator.domain.model.EstimateItem
import com.koinvois.generator.domain.model.EstimatePhoto
import kotlinx.coroutines.flow.Flow

interface EstimateRepository {
    fun observeAllEstimates(): Flow<List<Estimate>>
    suspend fun getAllEstimates(): List<Estimate>
    suspend fun getEstimateById(estimateId: Int): Estimate
    suspend fun insertEstimate(estimate: Estimate)
    suspend fun updateEstimate(estimate: Estimate)
    suspend fun deleteEstimate(estimate: Estimate)
    suspend fun deleteEstimateWithID(id: Long)

    suspend fun getAllEstimateItems(): List<EstimateItem>
    suspend fun getEstimateItemsByEstimateId(estimateId: Int): List<EstimateItem>
    suspend fun insertEstimateItem(estimateItem: EstimateItem)
    suspend fun insertAllEstimateItems(estimateItems: List<EstimateItem>)
    suspend fun updateEstimateItem(estimateItem: EstimateItem)
    suspend fun deleteEstimateItem(estimateItem: EstimateItem)
    suspend fun deleteEstimateItems(estimateId: Long)

    suspend fun getAllEstimatePhotos(): List<EstimatePhoto>
    suspend fun getEstimatePhotosByEstimateId(estimateId: Int): List<EstimatePhoto>
    suspend fun insertEstimatePhoto(estimatePhoto: EstimatePhoto)
    suspend fun insertEstimatePhotoList(estimatePhotoList: List<EstimatePhoto>)
    suspend fun updateEstimatePhoto(estimatePhoto: EstimatePhoto)
    suspend fun deleteEstimatePhoto(estimatePhoto: EstimatePhoto)
    suspend fun deleteEstimatePhotos(estimateId: Long)
}