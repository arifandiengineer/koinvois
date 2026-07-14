package com.koinvois.generator.core.di

import android.content.Context
import androidx.room.Room
import com.koinvois.generator.database.AppDataBase
import com.koinvois.generator.database.daos.ClientDao
import com.koinvois.generator.database.daos.EstimateDao
import com.koinvois.generator.database.daos.EstimateItemDao
import com.koinvois.generator.database.daos.EstimatePhotoDao
import com.koinvois.generator.database.daos.InvoiceDao
import com.koinvois.generator.database.daos.InvoiceItemDao
import com.koinvois.generator.database.daos.InvoicePhotoDao
import com.koinvois.generator.database.daos.ItemDao
import com.koinvois.generator.database.daos.PersonalBusinessDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Relocated from di/Module.kt (Sprint 1 project-structure move, no behavior
 * change). Repository bindings live in RepositoryModule.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "invoice-make").build()
    }

    @Provides
    fun provideClientDao(db: AppDataBase): ClientDao = db.clientDao()

    @Provides
    fun provideItemDao(db: AppDataBase): ItemDao = db.itemDao()

    @Provides
    fun providePersonalBusinessDao(db: AppDataBase): PersonalBusinessDao = db.personalBusinessDao()

    @Provides
    fun provideInvoiceDao(db: AppDataBase): InvoiceDao = db.invoiceDao()

    @Provides
    fun provideInvoiceItemDao(db: AppDataBase): InvoiceItemDao = db.invoiceItemDao()

    @Provides
    fun provideInvoicePhotoDao(db: AppDataBase): InvoicePhotoDao = db.invoicePhotoDao()

    @Provides
    fun provideEstimateDao(db: AppDataBase): EstimateDao = db.estimateDao()

    @Provides
    fun provideEstimateItemDao(db: AppDataBase): EstimateItemDao = db.estimateItemDao()

    @Provides
    fun provideEstimatePhotoDao(db: AppDataBase): EstimatePhotoDao = db.estimatePhotoDao()
}