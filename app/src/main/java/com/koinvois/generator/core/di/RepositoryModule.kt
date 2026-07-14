package com.koinvois.generator.core.di

import com.koinvois.generator.data.repository.ClientRepositoryImpl
import com.koinvois.generator.data.repository.EstimateRepositoryImpl
import com.koinvois.generator.data.repository.InvoiceRepositoryImpl
import com.koinvois.generator.data.repository.ItemRepositoryImpl
import com.koinvois.generator.data.repository.PersonalBusinessRepositoryImpl
import com.koinvois.generator.domain.repository.ClientRepository
import com.koinvois.generator.domain.repository.EstimateRepository
import com.koinvois.generator.domain.repository.InvoiceRepository
import com.koinvois.generator.domain.repository.ItemRepository
import com.koinvois.generator.domain.repository.PersonalBusinessRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindClientRepository(impl: ClientRepositoryImpl): ClientRepository

    @Binds
    abstract fun bindItemRepository(impl: ItemRepositoryImpl): ItemRepository

    @Binds
    abstract fun bindPersonalBusinessRepository(impl: PersonalBusinessRepositoryImpl): PersonalBusinessRepository

    @Binds
    abstract fun bindInvoiceRepository(impl: InvoiceRepositoryImpl): InvoiceRepository

    @Binds
    abstract fun bindEstimateRepository(impl: EstimateRepositoryImpl): EstimateRepository
}