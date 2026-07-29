package com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_remote_data_source

import com.mohamed.dailynews.data.api.WebServices
import com.mohamed.dailynews.data.api.model.ArticlesResponse
import com.mohamed.dailynews.data.api.model.SourcesResponse
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(private val services: WebServices) :
    NewsRemoteDataSource {

    override suspend fun getSources(category: String): SourcesResponse {
        return services.getSources(category = category)
    }

    override suspend fun getArticles(source: String): ArticlesResponse {
        return services.getArticles(source = source)
    }

    override suspend fun searchArticles(query: String): ArticlesResponse {
        return services.searchArticles(query = query)
    }
}