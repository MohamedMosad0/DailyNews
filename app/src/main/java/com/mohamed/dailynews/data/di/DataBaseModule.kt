package com.mohamed.dailynews.data.di

import android.content.Context
import androidx.room.Room
import com.mohamed.dailynews.data.database.MyDatabase
import com.mohamed.dailynews.data.database.dao.SourcesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    fun provideMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "daily_news.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(myDatabase: MyDatabase): SourcesDao {
        return myDatabase.getSourcesDao()
    }
}