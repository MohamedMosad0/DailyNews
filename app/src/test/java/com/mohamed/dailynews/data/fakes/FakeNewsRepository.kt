package com.mohamed.dailynews.data.fakes

import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.model.Source
import com.mohamed.dailynews.domain.repository.NewsRepository

class FakeNewsRepository : NewsRepository {
    var sourcesToReturn: List<Source> = emptyList()
    var articlesToReturn: List<Article> = emptyList()
    var searchArticlesToReturn: List<Article> = emptyList()
    var shouldThrowException: Boolean = false
    var exceptionToThrow: Throwable = RuntimeException("Repository error")

    override suspend fun getSources(category: String): List<Source> {
        if (shouldThrowException) throw exceptionToThrow
        return sourcesToReturn
    }

    override suspend fun getArticles(source: String): List<Article> {
        if (shouldThrowException) throw exceptionToThrow
        return articlesToReturn
    }

    override suspend fun searchArticles(query: String): List<Article> {
        if (shouldThrowException) throw exceptionToThrow
        return searchArticlesToReturn
    }
}
