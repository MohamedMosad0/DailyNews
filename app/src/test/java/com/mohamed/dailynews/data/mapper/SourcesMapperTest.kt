package com.mohamed.dailynews.data.mapper

import com.mohamed.dailynews.data.api.model.SourceDto
import com.mohamed.dailynews.data.database.entity.SourceEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SourcesMapperTest {

    @Test
    fun `toEntity maps valid SourceDto to SourceEntity`() {
        val dto = SourceDto(
            id = "bbc-news",
            name = "BBC News",
            description = "General news",
            url = "https://bbc.com",
            category = "general",
            language = "en",
            country = "gb"
        )

        val entity = dto.toEntity(category = "general")

        assertEquals("bbc-news", entity?.id)
        assertEquals("BBC News", entity?.name)
        assertEquals("General news", entity?.description)
        assertEquals("https://bbc.com", entity?.url)
        assertEquals("general", entity?.category)
        assertEquals("en", entity?.language)
        assertEquals("gb", entity?.country)
    }

    @Test
    fun `toEntity returns null when id is null or blank`() {
        val nullIdDto = SourceDto(id = null, name = "Invalid")
        val blankIdDto = SourceDto(id = "   ", name = "Invalid")

        assertNull(nullIdDto.toEntity("general"))
        assertNull(blankIdDto.toEntity("general"))
    }

    @Test
    fun `toEntities filters out invalid DTOs`() {
        val list = listOf(
            SourceDto(id = "s1", name = "Source 1"),
            SourceDto(id = "", name = "Source 2"),
            SourceDto(id = null, name = "Source 3"),
            SourceDto(id = "s4", name = "Source 4")
        )

        val result = list.toEntities(category = "sports")

        assertEquals(2, result.size)
        assertEquals("s1", result[0].id)
        assertEquals("s4", result[1].id)
    }

    @Test
    fun `toDomain maps SourceEntity to Source domain model`() {
        val entity = SourceEntity(
            id = "tech-crunch",
            name = "TechCrunch",
            description = "Tech news",
            url = "https://techcrunch.com",
            category = "technology",
            language = "en",
            country = "us"
        )

        val domain = entity.toDomain()

        assertEquals("tech-crunch", domain.id)
        assertEquals("TechCrunch", domain.name)
    }
}
