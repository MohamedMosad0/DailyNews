package com.mohamed.dailynews.data.mapper

import com.mohamed.dailynews.data.api.model.ArticleDM
import com.mohamed.dailynews.domain.model.Article

fun ArticleDM.toDomain(): Article {
    return Article(
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        content = content,
        author = author,
        publishedAt = publishedAt,
        sourceName = source?.name
    )
}

fun List<ArticleDM>?.toDomainArticles(): List<Article> {
    return this?.map { it.toDomain() } ?: emptyList()
}
