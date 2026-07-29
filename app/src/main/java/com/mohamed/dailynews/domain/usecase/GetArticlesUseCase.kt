package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.repository.NewsRepository
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend fun execute(source: String): List<Article> = repository.getArticles(source)
}
