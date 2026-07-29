package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.repository.NewsRepository
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend fun execute(query: String): List<Article> = repository.searchArticles(query)
}
