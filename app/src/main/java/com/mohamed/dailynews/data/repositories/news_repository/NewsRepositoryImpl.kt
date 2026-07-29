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
        Timber.tag("NewsRepository").d("getSources category='$category', isConnected=$isConnected")
        return if (isConnected) {
            Timber.tag("NewsRepository").d("Executing RemoteDataSource branch for category='$category'")
            val sourcesResponse = remoteDataSource.getSources(category)
            val sourcesList = sourcesResponse.sources ?: emptyList()
            Timber.tag("NewsRepository").d("Remote response status='${sourcesResponse.status}', count=${sourcesList.size}, sources=$sourcesList")
            localDataSource.saveSources(category, sourcesList)
            sourcesMapper.toSources(sourcesList)
        } else {
            Timber.tag("NewsRepository").d("Executing LocalDataSource branch for category='$category'")
            val sources = localDataSource.getSources(category)
            Timber.tag("NewsRepository").d("Local database sources count=${sources.size}")
            sourcesMapper.toSources(sources)
        }
    }

    override suspend fun getArticles(source: String): List<Article> {
        Timber.tag("NewsRepository").d("getArticles source='$source'")
        val response = remoteDataSource.getArticles(source = source)
        Timber.tag("NewsRepository").d("getArticles response status='${response.status}', count=${response.articles?.size}")
        return response.articles.toDomainArticles()
    }

    override suspend fun searchArticles(query: String): List<Article> {
        Timber.tag("NewsRepository").d("searchArticles query='$query'")
        val response = remoteDataSource.searchArticles(query = query)
        Timber.tag("NewsRepository").d("searchArticles response status='${response.status}', totalResults=${response.totalResults}, count=${response.articles?.size}")
        return response.articles.toDomainArticles()
    }
}