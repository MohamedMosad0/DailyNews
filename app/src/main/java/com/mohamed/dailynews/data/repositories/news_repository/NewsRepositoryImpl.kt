package com.mohamed.dailynews.data.repositories.news_repository

import com.mohamed.dailynews.data.mapper.toDomainArticles
import com.mohamed.dailynews.data.mapper.toDomainSources
import com.mohamed.dailynews.data.mapper.toEntities
import com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_local_data_source.NewsLocalDataSource
import com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_remote_data_source.NewsRemoteDataSource
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.model.Source
import com.mohamed.dailynews.domain.repository.NewsRepository
import com.mohamed.dailynews.utils.Connectivity
import com.mohamed.dailynews.utils.error.DataError
import com.mohamed.dailynews.utils.error.DataException
import com.mohamed.dailynews.utils.error.toDataError
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val localDataSource: NewsLocalDataSource,
    private val remoteDataSource: NewsRemoteDataSource,
    private val connectivity: Connectivity
) : NewsRepository {

    override suspend fun getSources(category: String): List<Source> {
        val isConnected = connectivity.isOnline()
        Timber.tag("NewsRepository").d("getSources category='$category', isConnected=$isConnected")

        if (isConnected) {
            try {
                val sourcesResponse = remoteDataSource.getSources(category)
                val sourcesList = sourcesResponse.sources ?: emptyList()
                val entities = sourcesList.toEntities(category)
                localDataSource.replaceSources(category, entities)
                return entities.toDomainSources()
            } catch (t: Throwable) {
                Timber.tag("NewsRepository").e(t, "Remote fetch failed for getSources, attempting local cache fallback")
                val cached = localDataSource.getSources(category)
                if (cached.isNotEmpty()) {
                    return cached.toDomainSources()
                }
                throw DataException(t.toDataError())
            }
        } else {
            val cached = localDataSource.getSources(category)
            if (cached.isNotEmpty()) {
                return cached.toDomainSources()
            }
            throw DataException(DataError.NoCache)
        }
    }

    override suspend fun getArticles(source: String): List<Article> {
        val isConnected = connectivity.isOnline()
        Timber.tag("NewsRepository").d("getArticles source='$source', isConnected=$isConnected")

        if (isConnected) {
            try {
                val response = remoteDataSource.getArticles(source = source)
                val articlesList = response.articles ?: emptyList()
                val entities = articlesList.toEntities(defaultSourceId = source)
                localDataSource.replaceArticles(source, entities)
                return entities.toDomainArticles()
            } catch (t: Throwable) {
                Timber.tag("NewsRepository").e(t, "Remote fetch failed for getArticles, attempting local cache fallback")
                val cached = localDataSource.getArticles(source)
                if (cached.isNotEmpty()) {
                    return cached.toDomainArticles()
                }
                throw DataException(t.toDataError())
            }
        } else {
            val cached = localDataSource.getArticles(source)
            if (cached.isNotEmpty()) {
                return cached.toDomainArticles()
            }
            throw DataException(DataError.NoCache)
        }
    }

    override suspend fun searchArticles(query: String): List<Article> {
        val isConnected = connectivity.isOnline()
        Timber.tag("NewsRepository").d("searchArticles query='$query', isConnected=$isConnected")

        if (isConnected) {
            try {
                val response = remoteDataSource.searchArticles(query = query)
                val articlesList = response.articles ?: emptyList()
                val entities = articlesList.toEntities(defaultSourceId = "search")
                if (entities.isNotEmpty()) {
                    localDataSource.insertArticles(entities)
                }
                return entities.toDomainArticles()
            } catch (t: Throwable) {
                Timber.tag("NewsRepository").e(t, "Remote search failed, attempting offline search fallback")
                val cachedMatches = localDataSource.searchArticles(query)
                if (cachedMatches.isNotEmpty()) {
                    return cachedMatches.toDomainArticles()
                }
                throw DataException(t.toDataError())
            }
        } else {
            val cachedMatches = localDataSource.searchArticles(query)
            if (cachedMatches.isNotEmpty()) {
                return cachedMatches.toDomainArticles()
            }
            throw DataException(DataError.NoCache)
        }
    }
}