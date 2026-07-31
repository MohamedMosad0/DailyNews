package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.data.fakes.FakeNewsRepository
import com.mohamed.dailynews.domain.model.Article
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetArticlesUseCaseTest {

    private lateinit var repository: FakeNewsRepository
    private lateinit var useCase: GetArticlesUseCase

    @Before
    fun setUp() {
        repository = FakeNewsRepository()
        useCase = GetArticlesUseCase(repository)
    }

    @Test
    fun `execute returns list of articles from repository`() = runTest {
        val expected = listOf(
            Article(title = "Article 1", url = "https://a.com/1"),
            Article(title = "Article 2", url = "https://a.com/2")
        )
        repository.articlesToReturn = expected

        val actual = useCase.execute("bbc-news")

        assertEquals(expected, actual)
    }
}
