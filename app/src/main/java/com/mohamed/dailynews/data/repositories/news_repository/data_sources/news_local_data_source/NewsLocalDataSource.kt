package com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_local_data_source

import com.mohamed.dailynews.data.database.entity.ArticleEntity
import com.mohamed.dailynews.data.database.entity.SourceEntity

interface NewsLocalDataSource {
    suspend fun getSources(category: String): List<SourceEntity>
    suspend fun getAllSources(): List<SourceEntity>
    suspend fun replaceSources(category: String, sources: List<SourceEntity>)
    suspend fun getArticles(sourceId: String): List<ArticleEntity>
    suspend fun insertArticles(articles: List<ArticleEntity>)
    suspend fun replaceArticles(sourceId: String, articles: List<ArticleEntity>)
    suspend fun searchArticles(query: String): List<ArticleEntity>
}