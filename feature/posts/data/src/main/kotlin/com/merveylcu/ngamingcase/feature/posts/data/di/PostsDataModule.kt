package com.merveylcu.ngamingcase.feature.posts.data.di

import com.merveylcu.ngamingcase.feature.posts.data.remote.PostApi
import com.merveylcu.ngamingcase.feature.posts.data.repository.PostRepositoryImpl
import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import com.merveylcu.ngamingcase.network.extensions.create
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PostsDataModule {
    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    companion object {
        @Provides
        @Singleton
        fun providePostApi(retrofit: Retrofit): PostApi = retrofit.create()
    }
}
