package com.koinvois.generator.domain.repository

import com.koinvois.generator.domain.model.PersonalBusiness
import kotlinx.coroutines.flow.Flow

interface PersonalBusinessRepository {
    fun observeAllBusiness(): Flow<List<PersonalBusiness>>
    suspend fun getAllBusiness(): List<PersonalBusiness>
    suspend fun getBusinessById(businessId: Int): PersonalBusiness
    suspend fun getBusinessByName(businessName: String): PersonalBusiness?
    suspend fun insertBusiness(business: PersonalBusiness)
    suspend fun updateBusiness(business: PersonalBusiness)
    suspend fun deleteBusiness(business: PersonalBusiness)
}