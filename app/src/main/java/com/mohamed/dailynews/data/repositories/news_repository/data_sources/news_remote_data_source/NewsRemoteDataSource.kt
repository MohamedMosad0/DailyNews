package com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_remote_data_source

import com.mohamed.dailynews.data.api.model.ArticlesResponse
import com.mohamed.dailynews.data.api.model.SourcesResponse

interface NewsRemoteDataSource {
    suspend fun getSources(category: String): SourcesResponse
    suspend fun getArticles(source: String): ArticlesResponse
    suspend fun searchArticles(query: String): ArticlesResponse
}