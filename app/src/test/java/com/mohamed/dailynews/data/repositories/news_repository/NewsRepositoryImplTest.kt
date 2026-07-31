package com.mohamed.dailynews.data.repositories.news_repository

import com.mohamed.dailynews.data.api.model.ArticleDto
import com.mohamed.dailynews.data.api.model.ArticlesResponse
import com.mohamed.dailynews.data.api.model.SourceDto
import com.mohamed.dailynews.data.api.model.SourcesResponse
import com.mohamed.dailynews.data.database.entity.ArticleEntity
import com.mohamed.dailynews.data.database.entity.SourceEntity
import com.mohamed.dailynews.data.fakes.FakeConnectivity
import com.mohamed.dailynews.data.fakes.FakeNewsLocalDataSource
import com.mohamed.dailynews.data.fakes.FakeNewsRemoteDataSource
import com.mohamed.dailynews.utils.error.DataError
import com.mohamed.dailynews.utils.error.DataException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {

    private lateinit var localDataSource: FakeNewsLocalDataSource
    private lateinit var remoteDataSource: FakeNewsRemoteDataSource
    private lateinit var connectivity: FakeConnectivity
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setUp() {
        localDataSource = FakeNewsLocalDataSource()
        remoteDataSource = FakeNewsRemoteDataSource()
        connectivity = FakeConnectivity(isOnlineValue = true)
        repository = NewsRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            connectivity = connectivity
        )
    }

    @Test
    fun `getSources when online success saves to cache and returns domain models`() = runTest {
        remoteDataSource.sourcesResponseToReturn = SourcesResponse(
            sources = listOf(
                SourceDto(id = "bbc-news", name = "BBC News")
            )
        )

        val sources = repository.getSources("general")

        assertEquals(1, sources.size)
        assertEquals("bbc-news", sources[0].id)
        assertEquals("BBC News", sources[0].name)

        val cached = localDataSource.getSources("general")
        assertEquals(1, cached.size)
        assertEquals("bbc-news", cached[0].id)
    }

    @Test
    fun `getSources when online fails falls back to local cache`() = runTest {
        localDataSource.replaceSources("general", listOf(SourceEntity(id = "cached-id", name = "Cached Name", description = null, url = null, category = "general", language = null, country = null)))
        remoteDataSource.shouldThrowException = true

        val sources = repository.getSources("general")

        assertEquals(1, sources.size)
        assertEquals("cached-id", sources[0].id)
    }

    @Test
    fun `getSources when online fails and no cache throws DataException`() = runTest {
        remoteDataSource.shouldThrowException = true

        try {
            repository.getSources("general")
            fail("Expected DataException was not thrown")
        } catch (e: DataException) {
            assertEquals(DataError.Unknown("Remote network error", remoteDataSource.exceptionToThrow), e.error)
        }
    }

    @Test
    fun `getSources when offline and no cache throws DataException with NoCache error`() = runTest {
        connectivity.isOnlineValue = false

        try {
            repository.getSources("sports")
            fail("Expected DataException was not thrown")
        } catch (e: DataException) {
            assertEquals(DataError.NoCache, e.error)
        }
    }

    @Test
    fun `getArticles when online success saves to cache and returns domain models`() = runTest {
        remoteDataSource.articlesResponseToReturn = ArticlesResponse(
            articles = listOf(
                ArticleDto(url = "https://article.com/1", title = "Online Article", source = SourceDto(id = "bbc", name = "BBC"))
            )
        )

        val articles = repository.getArticles("bbc")

        assertEquals(1, articles.size)
        assertEquals("Online Article", articles[0].title)

        val cached = localDataSource.getArticles("bbc")
        assertEquals(1, cached.size)
        assertEquals("https://article.com/1", cached[0].url)
    }

    @Test
    fun `getArticles when online fails falls back to local cache`() = runTest {
        localDataSource.replaceArticles(
            "bbc",
            listOf(
                ArticleEntity(
                    url = "https://cached.com/1",
                    sourceId = "bbc",
                    sourceName = "BBC",
                    title = "Cached Article",
                    description = "Desc",
                    urlToImage = null,
                    author = null,
                    publishedAt = null,
                    content = null
                )
            )
        )
        remoteDataSource.shouldThrowException = true

        val articles = repository.getArticles("bbc")

        assertEquals(1, articles.size)
        assertEquals("Cached Article", articles[0].title)
    }

    @Test
    fun `getArticles when offline and no cache throws DataException`() = runTest {
        connectivity.isOnlineValue = false

        try {
            repository.getArticles("bbc")
            fail("Expected DataException was not thrown")
        } catch (e: DataException) {
            assertEquals(DataError.NoCache, e.error)
        }
    }

    @Test
    fun `searchArticles when online success inserts articles into local cache`() = runTest {
        remoteDataSource.searchResponseToReturn = ArticlesResponse(
            articles = listOf(
                ArticleDto(url = "https://search.com/1", title = "Kotlin 2.2 Features", source = SourceDto(id = "tech", name = "Tech"))
            )
        )

        val results = repository.searchArticles("Kotlin")

        assertEquals(1, results.size)
        assertEquals("Kotlin 2.2 Features", results[0].title)
    }

    @Test
    fun `searchArticles when online fails falls back to offline search across cached articles`() = runTest {
        localDataSource.insertArticles(
            listOf(
                ArticleEntity(
                    url = "https://cached-search.com/1",
                    sourceId = "tech",
                    sourceName = "Tech",
                    title = "Kotlin Multiplatform",
                    description = "Cross platform development",
                    urlToImage = null,
                    author = null,
                    publishedAt = null,
                    content = null
                )
            )
        )
        remoteDataSource.shouldThrowException = true

        val results = repository.searchArticles("Kotlin")

        assertEquals(1, results.size)
        assertEquals("Kotlin Multiplatform", results[0].title)
    }

    @Test
    fun `searchArticles when offline searches cached articles`() = runTest {
        connectivity.isOnlineValue = false
        localDataSource.insertArticles(
            listOf(
                ArticleEntity(
                    url = "https://search.com/1",
                    sourceId = "tech",
                    sourceName = "Tech",
                    title = "Kotlin 2.2 Released",
                    description = "New features in Kotlin",
                    urlToImage = null,
                    author = null,
                    publishedAt = null,
                    content = null
                )
            )
        )

        val results = repository.searchArticles("Kotlin")

        assertEquals(1, results.size)
        assertEquals("Kotlin 2.2 Released", results[0].title)
    }
}
