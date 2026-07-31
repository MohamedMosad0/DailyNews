package com.mohamed.dailynews.data.mapper

import com.mohamed.dailynews.data.api.model.ArticleDto
import com.mohamed.dailynews.data.database.entity.ArticleEntity
import com.mohamed.dailynews.domain.model.Article

fun ArticleDto.toEntity(defaultSourceId: String): ArticleEntity? {
    val safeUrl = url?.takeIf { it.isNotBlank() } ?: return null
    val effectiveSourceId = source?.id?.takeIf { it.isNotBlank() }
        ?: source?.name?.takeIf { it.isNotBlank() }
        ?: defaultSourceId

    return ArticleEntity(
        url = safeUrl,
        sourceId = effectiveSourceId,
        sourceName = source?.name,
        title = title,
        description = description,
        urlToImage = urlToImage,
        author = author,
        publishedAt = publishedAt,
        content = content
    )
}

fun List<ArticleDto>?.toEntities(defaultSourceId: String): List<ArticleEntity> {
    return this?.mapNotNull { it.toEntity(defaultSourceId) } ?: emptyList()
}

fun ArticleEntity.toDomain(): Article {
    return Article(
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        content = content,
        author = author,
        publishedAt = publishedAt,
        sourceName = sourceName
    )
}

fun List<ArticleEntity>?.toDomainArticles(): List<Article> {
    return this?.map { it.toDomain() } ?: emptyList()
}
