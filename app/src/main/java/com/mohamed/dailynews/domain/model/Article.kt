package com.mohamed.dailynews.domain.model

data class Article(
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val content: String? = null,
    val author: String? = null,
    val publishedAt: String? = null,
    val sourceName: String? = null
)
