package com.mohamed.dailynews.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohamed.dailynews.data.database.dao.ArticlesDao
import com.mohamed.dailynews.data.database.dao.SourcesDao
import com.mohamed.dailynews.data.database.entity.ArticleEntity
import com.mohamed.dailynews.data.database.entity.SourceEntity

@Database(entities = [SourceEntity::class, ArticleEntity::class], version = 2, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getSourcesDao(): SourcesDao
    abstract fun getArticlesDao(): ArticlesDao
}
