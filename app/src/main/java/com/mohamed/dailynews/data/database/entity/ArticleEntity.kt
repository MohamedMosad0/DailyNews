package com.mohamed.dailynews.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles",
    indices = [Index(value = ["source_id"])]
)
data class ArticleEntity(
    @PrimaryKey
    val url: String,

    @ColumnInfo(name = "source_id")
    val sourceId: String,

    @ColumnInfo(name = "source_name")
    val sourceName: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "url_to_image")
    val urlToImage: String?,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "published_at")
    val publishedAt: String?,

    @ColumnInfo(name = "content")
    val content: String?,

    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = System.currentTimeMillis()
)
