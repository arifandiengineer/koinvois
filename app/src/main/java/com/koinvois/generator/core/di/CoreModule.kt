package com.koinvois.generator.core.di

import com.koinvois.generator.core.utils.ResourceProvider
import com.koinvois.generator.core.utils.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Binds core/utils interfaces to their implementations for app-wide injection. */
@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindResourceProvider(impl: ResourceProviderImpl): ResourceProvider
}
