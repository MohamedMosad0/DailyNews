package com.mohamed.dailynews.data.mapper

import com.mohamed.dailynews.data.api.model.SourceDto
import com.mohamed.dailynews.data.database.entity.SourceEntity
import com.mohamed.dailynews.domain.model.Source

fun SourceDto.toEntity(category: String): SourceEntity? {
    val safeId = id?.takeIf { it.isNotBlank() } ?: return null
    return SourceEntity(
        id = safeId,
        name = name,
        description = description,
        url = url,
        category = category,
        language = language,
        country = country
    )
}

fun List<SourceDto>?.toEntities(category: String): List<SourceEntity> {
    return this?.mapNotNull { it.toEntity(category) } ?: emptyList()
}

fun SourceEntity.toDomain(): Source {
    return Source(
        id = id,
        name = name
    )
}

fun List<SourceEntity>?.toDomainSources(): List<Source> {
    return this?.map { it.toDomain() } ?: emptyList()
}