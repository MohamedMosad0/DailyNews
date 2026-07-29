package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.Source
import com.mohamed.dailynews.domain.repository.NewsRepository
import javax.inject.Inject

class GetSourcesUseCase @Inject constructor(var repository: NewsRepository) {

    suspend fun execute(category: String): List<Source> = repository.getSources(category)
}