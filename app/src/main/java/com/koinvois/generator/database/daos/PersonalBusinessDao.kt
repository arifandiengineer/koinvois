package com.koinvois.generator.database.daos

import androidx.room.*
import com.koinvois.generator.database.models.PersonalBusiness
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalBusinessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: PersonalBusiness)

    @Query("SELECT * FROM personalBusiness WHERE business_name = :businessName")
    suspend fun getBusinessByName(businessName: String): PersonalBusiness?

    @Query("SELECT * FROM personalBusiness")
    suspend fun getAllBusiness(): List<PersonalBusiness>

    @Query("SELECT * FROM personalBusiness")
    fun observeAllBusiness(): Flow<List<PersonalBusiness>>

    @Query("SELECT * FROM personalBusiness WHERE Id = :businessId")
    suspend fun getBusinessById(businessId: Int): PersonalBusiness

    @Update
    suspend fun updateBusiness(vararg business: PersonalBusiness)

    @Delete
    suspend fun deleteBusiness(vararg business: PersonalBusiness)
}