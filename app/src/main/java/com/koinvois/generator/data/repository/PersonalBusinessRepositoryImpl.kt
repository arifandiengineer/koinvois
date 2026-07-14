package com.koinvois.generator.data.repository

import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.database.daos.PersonalBusinessDao
import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.repository.PersonalBusinessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonalBusinessRepositoryImpl @Inject constructor(
    private val personalBusinessDao: PersonalBusinessDao
) : PersonalBusinessRepository {

    override fun observeAllBusiness(): Flow<List<PersonalBusiness>> =
        personalBusinessDao.observeAllBusiness().map { it.toDomain() }

    override suspend fun getAllBusiness(): List<PersonalBusiness> =
        personalBusinessDao.getAllBusiness().toDomain()

    override suspend fun getBusinessById(businessId: Int): PersonalBusiness =
        personalBusinessDao.getBusinessById(businessId).toDomain()

    override suspend fun getBusinessByName(businessName: String): PersonalBusiness? =
        personalBusinessDao.getBusinessByName(businessName)?.toDomain()

    override suspend fun insertBusiness(business: PersonalBusiness) =
        personalBusinessDao.insertBusiness(business.toEntity())

    override suspend fun updateBusiness(business: PersonalBusiness) =
        personalBusinessDao.updateBusiness(business.toEntity())

    override suspend fun deleteBusiness(business: PersonalBusiness) =
        personalBusinessDao.deleteBusiness(business.toEntity())
}