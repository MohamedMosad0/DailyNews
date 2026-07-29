package com.mohamed.dailynews.data.repositories.news_repository

import com.mohamed.dailynews.data.mapper.SourcesMapper
import com.mohamed.dailynews.data.mapper.toDomainArticles
import com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_local_data_source.NewsLocalDataSource
import com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_remote_data_source.NewsRemoteDataSource
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.model.Source
import com.mohamed.dailynews.domain.repository.NewsRepository
import com.mohamed.dailynews.utils.Connectivity
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val localDataSource: NewsLocalDataSource,
    private val remoteDataSource: NewsRemoteDataSource,
    private val connectivity: Connectivity,
    private val sourcesMapper: SourcesMapper
) : NewsRepository {

    override suspend fun getSources(category: String): List<Source> {
        val isConnected = connectivity.isOnline()
        Timber.tag("NewsRepository").e("isConnected = $isConnected")
        return if (isConnected) {
            val sourcesResponse = remoteDataSource.getSources(category)
            val sourcesList = sourcesResponse.sources ?: emptyList()
            localDataSource.saveSources(category, sourcesList)
            sourcesMapper.toSources(sourcesList)
        } else {
            val sources = localDataSource.getSources(category)
            sourcesMapper.toSources(sources)
        }
    }

    override suspend fun getArticles(source: String): List<Article> {
        val response = remoteDataSource.getArticles(source = source)
        return response.articles.toDomainArticles()
    }

    override suspend fun searchArticles(query: String): List<Article> {
        val response = remoteDataSource.searchArticles(query = query)
        return response.articles.toDomainArticles()
    }
}