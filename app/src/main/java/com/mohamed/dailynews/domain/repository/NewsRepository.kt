package com.mohamed.dailynews.domain.repository

import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.model.Source

interface NewsRepository {
    suspend fun getSources(category: String): List<Source>
    suspend fun getArticles(source: String): List<Article>
    suspend fun searchArticles(query: String): List<Article>
}