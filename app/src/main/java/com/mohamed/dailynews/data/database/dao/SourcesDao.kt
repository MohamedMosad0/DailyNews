package com.mohamed.dailynews.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mohamed.dailynews.data.database.entity.SourceEntity

@Dao
interface SourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSources(sources: List<SourceEntity>)

    @Query("SELECT * FROM sources WHERE category = :category")
    suspend fun getSources(category: String): List<SourceEntity>

    @Query("SELECT * FROM sources")
    suspend fun getAllSources(): List<SourceEntity>

    @Query("DELETE FROM sources WHERE category = :category")
    suspend fun deleteSourcesByCategory(category: String)

    @Transaction
    suspend fun replaceSourcesByCategory(category: String, sources: List<SourceEntity>) {
        deleteSourcesByCategory(category)
        saveSources(sources)
    }
}