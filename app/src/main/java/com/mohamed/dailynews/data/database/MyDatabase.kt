package com.mohamed.dailynews.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohamed.dailynews.data.api.model.SourceDM
import com.mohamed.dailynews.data.database.dao.SourcesDao

@Database(entities = [SourceDM::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getSourcesDao(): SourcesDao
}
