package com.mohamed.dailynews.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mohamed.dailynews.data.database.entity.ArticleEntity

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles WHERE source_id = :sourceId ORDER BY published_at DESC")
    suspend fun getArticlesBySource(sourceId: String): List<ArticleEntity>

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY published_at DESC")
    suspend fun searchArticles(query: String): List<ArticleEntity>

    @Query("DELETE FROM articles WHERE source_id = :sourceId")
    suspend fun deleteArticlesBySource(sourceId: String)

    @Transaction
    suspend fun replaceArticlesBySource(sourceId: String, articles: List<ArticleEntity>) {
        deleteArticlesBySource(sourceId)
        insertArticles(articles)
    }
}
