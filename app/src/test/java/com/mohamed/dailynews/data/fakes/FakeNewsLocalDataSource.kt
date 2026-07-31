package com.mohamed.dailynews.data.fakes

import com.mohamed.dailynews.data.database.entity.ArticleEntity
import com.mohamed.dailynews.data.database.entity.SourceEntity
import com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_local_data_source.NewsLocalDataSource

class FakeNewsLocalDataSource : NewsLocalDataSource {
    private val sourcesMap = mutableMapOf<String, MutableList<SourceEntity>>()
    private val articlesMap = mutableMapOf<String, MutableList<ArticleEntity>>()

    override suspend fun getSources(category: String): List<SourceEntity> {
        return sourcesMap[category.lowercase()] ?: emptyList()
    }

    override suspend fun getAllSources(): List<SourceEntity> {
        return sourcesMap.values.flatten()
    }

    override suspend fun replaceSources(category: String, sources: List<SourceEntity>) {
        sourcesMap[category.lowercase()] = sources.toMutableList()
    }

    override suspend fun getArticles(sourceId: String): List<ArticleEntity> {
        return articlesMap[sourceId] ?: emptyList()
    }

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        articles.forEach { article ->
            val list = articlesMap.getOrPut(article.sourceId) { mutableListOf() }
            list.removeAll { it.url == article.url }
            list.add(article)
        }
    }

    override suspend fun replaceArticles(sourceId: String, articles: List<ArticleEntity>) {
        articlesMap[sourceId] = articles.toMutableList()
    }

    override suspend fun searchArticles(query: String): List<ArticleEntity> {
        val q = query.lowercase()
        return articlesMap.values.flatten().filter {
            (it.title?.lowercase()?.contains(q) == true) ||
                    (it.description?.lowercase()?.contains(q) == true)
        }
    }
}
