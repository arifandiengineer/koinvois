package com.koinvois.generator.data.repository

import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toDomainItems
import com.koinvois.generator.data.mapper.toDomainPhotos
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.database.daos.EstimateDao
import com.koinvois.generator.database.daos.EstimateItemDao
import com.koinvois.generator.database.daos.EstimatePhotoDao
import com.koinvois.generator.domain.model.Estimate
import com.koinvois.generator.domain.model.EstimateItem
import com.koinvois.generator.domain.model.EstimatePhoto
import com.koinvois.generator.domain.repository.EstimateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EstimateRepositoryImpl @Inject constructor(
    private val estimateDao: EstimateDao,
    private val estimateItemDao: EstimateItemDao,
    private val estimatePhotoDao: EstimatePhotoDao
) : EstimateRepository {

    override fun observeAllEstimates(): Flow<List<Estimate>> =
        estimateDao.observeAllEstimates().map { it.toDomain() }

    override suspend fun getAllEstimates(): List<Estimate> =
        estimateDao.getAllEstimates().toDomain()

    override suspend fun getEstimateById(estimateId: Int): Estimate =
        estimateDao.getEstimateById(estimateId).toDomain()

    override suspend fun insertEstimate(estimate: Estimate) =
        estimateDao.insertEstimate(estimate.toEntity())

    override suspend fun updateEstimate(estimate: Estimate) =
        estimateDao.updateEstimate(estimate.toEntity())

    override suspend fun deleteEstimate(estimate: Estimate) =
        estimateDao.deleteEstimate(estimate.toEntity())

    override suspend fun deleteEstimateWithID(id: Long) =
        estimateDao.deleteEstimateWithID(id)

    override suspend fun getAllEstimateItems(): List<EstimateItem> =
        estimateItemDao.getAllEstimateItems().toDomainItems()

    override suspend fun getEstimateItemsByEstimateId(estimateId: Int): List<EstimateItem> =
        estimateItemDao.getEstimateItemsByEstimateId(estimateId).toDomainItems()

    override suspend fun insertEstimateItem(estimateItem: EstimateItem) =
        estimateItemDao.insertEstimateItem(estimateItem.toEntity())

    override suspend fun insertAllEstimateItems(estimateItems: List<EstimateItem>) =
        estimateItemDao.insertAllItems(estimateItems.map { it.toEntity() })

    override suspend fun updateEstimateItem(estimateItem: EstimateItem) =
        estimateItemDao.updateEstimateItem(estimateItem.toEntity())

    override suspend fun deleteEstimateItem(estimateItem: EstimateItem) =
        estimateItemDao.deleteEstimateItem(estimateItem.toEntity())

    override suspend fun deleteEstimateItems(estimateId: Long) =
        estimateItemDao.deleteEstimateItems(estimateId)

    override suspend fun getAllEstimatePhotos(): List<EstimatePhoto> =
        estimatePhotoDao.getAllEstimatePhotos().toDomainPhotos()

    override suspend fun getEstimatePhotosByEstimateId(estimateId: Int): List<EstimatePhoto> =
        estimatePhotoDao.getEstimatePhotosByEstimateId(estimateId).toDomainPhotos()

    override suspend fun insertEstimatePhoto(estimatePhoto: EstimatePhoto) =
        estimatePhotoDao.insertEstimatePhoto(estimatePhoto.toEntity())

    override suspend fun insertEstimatePhotoList(estimatePhotoList: List<EstimatePhoto>) =
        estimatePhotoDao.insertEstimatePhotoList(estimatePhotoList.map { it.toEntity() })

    override suspend fun updateEstimatePhoto(estimatePhoto: EstimatePhoto) =
        estimatePhotoDao.updateEstimatePhoto(estimatePhoto.toEntity())

    override suspend fun deleteEstimatePhoto(estimatePhoto: EstimatePhoto) =
        estimatePhotoDao.deleteEstimatePhoto(estimatePhoto.toEntity())

    override suspend fun deleteEstimatePhotos(estimateId: Long) =
        estimatePhotoDao.deleteEstimatePhotos(estimateId)
}