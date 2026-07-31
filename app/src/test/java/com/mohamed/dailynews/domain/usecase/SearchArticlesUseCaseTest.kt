package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.data.fakes.FakeNewsRepository
import com.mohamed.dailynews.domain.model.Article
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchArticlesUseCaseTest {

    private lateinit var repository: FakeNewsRepository
    private lateinit var useCase: SearchArticlesUseCase

    @Before
    fun setUp() {
        repository = FakeNewsRepository()
        useCase = SearchArticlesUseCase(repository)
    }

    @Test
    fun `execute returns search results from repository`() = runTest {
        val expected = listOf(
            Article(title = "Search Result 1", url = "https://a.com/search1")
        )
        repository.searchArticlesToReturn = expected

        val actual = useCase.execute("Android")

        assertEquals(expected, actual)
    }
}
