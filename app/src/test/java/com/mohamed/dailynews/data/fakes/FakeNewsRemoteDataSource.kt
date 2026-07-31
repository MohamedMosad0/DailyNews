package com.mohamed.dailynews.data.fakes

import com.mohamed.dailynews.data.api.model.ArticleDto
import com.mohamed.dailynews.data.api.model.ArticlesResponse
import com.mohamed.dailynews.data.api.model.SourceDto
import com.mohamed.dailynews.data.api.model.SourcesResponse
import com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_remote_data_source.NewsRemoteDataSource

class FakeNewsRemoteDataSource : NewsRemoteDataSource {
    var sourcesResponseToReturn: SourcesResponse = SourcesResponse(sources = emptyList())
    var articlesResponseToReturn: ArticlesResponse = ArticlesResponse(articles = emptyList())
    var searchResponseToReturn: ArticlesResponse = ArticlesResponse(articles = emptyList())
    var shouldThrowException: Boolean = false
    var exceptionToThrow: Throwable = RuntimeException("Remote network error")

    override suspend fun getSources(category: String): SourcesResponse {
        if (shouldThrowException) throw exceptionToThrow
        return sourcesResponseToReturn
    }

    override suspend fun getArticles(source: String): ArticlesResponse {
        if (shouldThrowException) throw exceptionToThrow
        return articlesResponseToReturn
    }

    override suspend fun searchArticles(query: String): ArticlesResponse {
        if (shouldThrowException) throw exceptionToThrow
        return searchResponseToReturn
    }
}
