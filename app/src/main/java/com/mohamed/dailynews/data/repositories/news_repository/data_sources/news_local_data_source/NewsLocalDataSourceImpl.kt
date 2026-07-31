package com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_local_data_source

import com.mohamed.dailynews.data.database.dao.ArticlesDao
import com.mohamed.dailynews.data.database.dao.SourcesDao
import com.mohamed.dailynews.data.database.entity.ArticleEntity
import com.mohamed.dailynews.data.database.entity.SourceEntity
import java.util.Locale.getDefault
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao
) : NewsLocalDataSource {

    override suspend fun getSources(category: String): List<SourceEntity> {
        return sourcesDao.getSources(category.lowercase(getDefault()))
    }

    override suspend fun getAllSources(): List<SourceEntity> {
        return sourcesDao.getAllSources()
    }

    override suspend fun replaceSources(category: String, sources: List<SourceEntity>) {
        sourcesDao.replaceSourcesByCategory(category.lowercase(getDefault()), sources)
    }

    override suspend fun getArticles(sourceId: String): List<ArticleEntity> {
        return articlesDao.getArticlesBySource(sourceId)
    }

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        articlesDao.insertArticles(articles)
    }

    override suspend fun replaceArticles(sourceId: String, articles: List<ArticleEntity>) {
        articlesDao.replaceArticlesBySource(sourceId, articles)
    }

    override suspend fun searchArticles(query: String): List<ArticleEntity> {
        return articlesDao.searchArticles(query)
    }
}