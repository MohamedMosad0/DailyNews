package com.mohamed.dailynews.data.repositories.news_repository.data_sources.news_local_data_source

import com.mohamed.dailynews.data.api.model.SourceDM

interface NewsLocalDataSource {
    suspend fun getSources(category: String): List<SourceDM>
    suspend fun getAllSources(): List<SourceDM>
    suspend fun saveSources(category: String, sources: List<SourceDM>)

}