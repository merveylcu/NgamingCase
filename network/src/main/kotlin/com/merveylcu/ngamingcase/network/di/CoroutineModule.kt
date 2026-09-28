package com.merveylcu.ngamingcase.network.di

import com.merveylcu.ngamingcase.core.common.qualifiers.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
public object CoroutineModule {
    @Provides
    @IoDispatcher
    public fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
