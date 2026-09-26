package com.merveylcu.ngamingcase.core.database.di

import android.content.Context
import androidx.room.Room
import com.merveylcu.ngamingcase.core.database.NgamingCaseDatabase
import com.merveylcu.ngamingcase.core.database.dao.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public object DatabaseModule {

    @Provides
    @Singleton
    public fun provideDatabase(@ApplicationContext context: Context): NgamingCaseDatabase = Room.databaseBuilder(
        context,
        NgamingCaseDatabase::class.java,
        "ngamingcase.db",
    ).build()

    @Provides
    public fun providePostDao(database: NgamingCaseDatabase): PostDao = database.postDao()
}
