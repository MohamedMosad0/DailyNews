package com.mohamed.dailynews.data.mapper

import com.mohamed.dailynews.data.api.model.ArticleDto
import com.mohamed.dailynews.data.api.model.SourceDto
import com.mohamed.dailynews.data.database.entity.ArticleEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArticlesMapperTest {

    @Test
    fun `toEntity maps valid ArticleDto to ArticleEntity`() {
        val dto = ArticleDto(
            source = SourceDto(id = "bbc", name = "BBC"),
            author = "John Doe",
            title = "Test Title",
            description = "Test Desc",
            url = "https://news.com/article1",
            urlToImage = "https://news.com/img.jpg",
            publishedAt = "2026-07-31T20:00:00Z",
            content = "Full content"
        )

        val entity = dto.toEntity(defaultSourceId = "default-src")

        assertEquals("https://news.com/article1", entity?.url)
        assertEquals("bbc", entity?.sourceId)
        assertEquals("BBC", entity?.sourceName)
        assertEquals("Test Title", entity?.title)
        assertEquals("Test Desc", entity?.description)
        assertEquals("https://news.com/img.jpg", entity?.urlToImage)
        assertEquals("John Doe", entity?.author)
        assertEquals("2026-07-31T20:00:00Z", entity?.publishedAt)
        assertEquals("Full content", entity?.content)
    }

    @Test
    fun `toEntity uses source name when source id is missing`() {
        val dto = ArticleDto(
            source = SourceDto(id = null, name = "CNN"),
            title = "Article",
            url = "https://news.com/article-cnn"
        )

        val entity = dto.toEntity(defaultSourceId = "fallback-source")

        assertEquals("CNN", entity?.sourceId)
    }

    @Test
    fun `toEntity falls back to defaultSourceId when source id and name are missing`() {
        val dto = ArticleDto(
            source = null,
            title = "No Source Article",
            url = "https://news.com/article2"
        )

        val entity = dto.toEntity(defaultSourceId = "fallback-source")

        assertEquals("fallback-source", entity?.sourceId)
    }

    @Test
    fun `toEntity returns null when url is null or blank`() {
        val nullUrlDto = ArticleDto(url = null, title = "Title")
        val blankUrlDto = ArticleDto(url = "   ", title = "Title")

        assertNull(nullUrlDto.toEntity("src"))
        assertNull(blankUrlDto.toEntity("src"))
    }

    @Test
    fun `toEntities filters out invalid DTOs with blank urls`() {
        val list = listOf(
            ArticleDto(url = "https://a.com/1", title = "Article 1"),
            ArticleDto(url = "", title = "Article 2"),
            ArticleDto(url = null, title = "Article 3"),
            ArticleDto(url = "https://a.com/4", title = "Article 4")
        )

        val result = list.toEntities(defaultSourceId = "src")

        assertEquals(2, result.size)
        assertEquals("https://a.com/1", result[0].url)
        assertEquals("https://a.com/4", result[1].url)
    }

    @Test
    fun `toDomain maps ArticleEntity to Article domain model`() {
        val entity = ArticleEntity(
            url = "https://a.com/1",
            sourceId = "bbc",
            sourceName = "BBC",
            title = "Headline",
            description = "Summary",
            urlToImage = "https://a.com/1.jpg",
            author = "Jane",
            publishedAt = "2026-07-31",
            content = "Body text"
        )

        val domain = entity.toDomain()

        assertEquals("Headline", domain.title)
        assertEquals("Summary", domain.description)
        assertEquals("https://a.com/1", domain.url)
        assertEquals("https://a.com/1.jpg", domain.urlToImage)
        assertEquals("Jane", domain.author)
        assertEquals("BBC", domain.sourceName)
    }
}
