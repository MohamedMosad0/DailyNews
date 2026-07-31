package com.mohamed.dailynews.data.di

import android.content.Context
import androidx.room.Room
import com.mohamed.dailynews.data.database.MyDatabase
import com.mohamed.dailynews.data.database.dao.ArticlesDao
import com.mohamed.dailynews.data.database.dao.SourcesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "daily_news.db")
            // Destructive migration is acceptable because the local database stores transient, re-fetchable news cache only.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSourcesDao(myDatabase: MyDatabase): SourcesDao {
        return myDatabase.getSourcesDao()
    }

    @Provides
    @Singleton
    fun provideArticlesDao(myDatabase: MyDatabase): ArticlesDao {
        return myDatabase.getArticlesDao()
    }
}