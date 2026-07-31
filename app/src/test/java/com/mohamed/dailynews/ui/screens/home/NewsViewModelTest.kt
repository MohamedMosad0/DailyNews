package com.mohamed.dailynews.ui.screens.home

import com.mohamed.dailynews.data.fakes.FakeNewsRepository
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.model.Source
import com.mohamed.dailynews.domain.usecase.GetArticlesUseCase
import com.mohamed.dailynews.domain.usecase.GetSourcesUseCase
import com.mohamed.dailynews.ui.screens.home.state.ArticlesUiState
import com.mohamed.dailynews.ui.screens.home.state.SourcesUiState
import com.mohamed.dailynews.utils.error.DataError
import com.mohamed.dailynews.utils.error.DataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeNewsRepository
    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeNewsRepository()
        viewModel = NewsViewModel(
            getSourcesUseCase = GetSourcesUseCase(repository),
            getArticlesUseCase = GetArticlesUseCase(repository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial sourcesUiState is Loading`() {
        assertEquals(SourcesUiState.Loading, viewModel.sourcesUiState.value)
    }

    @Test
    fun `initial articlesUiState is Initial`() {
        assertEquals(ArticlesUiState.Initial, viewModel.articlesUiState.value)
    }

    @Test
    fun `getSources emits Loading then Success state when repository succeeds`() = runTest {
        val sources = listOf(Source(id = "bbc", name = "BBC News"))
        repository.sourcesToReturn = sources

        viewModel.getSources("general")
        advanceUntilIdle()

        val state = viewModel.sourcesUiState.value
        assertTrue(state is SourcesUiState.Success)
        assertEquals(sources, (state as SourcesUiState.Success).sources)
    }

    @Test
    fun `getSources cancels previous job when called consecutively`() = runTest {
        val initialSources = listOf(Source(id = "1", name = "Source 1"))
        val finalSources = listOf(Source(id = "2", name = "Source 2"))

        repository.sourcesToReturn = initialSources
        viewModel.getSources("sports")

        repository.sourcesToReturn = finalSources
        viewModel.getSources("business")

        advanceUntilIdle()

        val state = viewModel.sourcesUiState.value
        assertTrue(state is SourcesUiState.Success)
        assertEquals(finalSources, (state as SourcesUiState.Success).sources)
    }

    @Test
    fun `getSources emits Error state when repository throws DataException`() = runTest {
        repository.shouldThrowException = true
        repository.exceptionToThrow = DataException(DataError.Offline)

        viewModel.getSources("general")
        advanceUntilIdle()

        val state = viewModel.sourcesUiState.value
        assertTrue(state is SourcesUiState.Error)
        assertEquals("No internet connection", (state as SourcesUiState.Error).message)
    }

    @Test
    fun `getArticles emits Loading then Success state when repository succeeds`() = runTest {
        val articles = listOf(Article(title = "Test Headline", url = "https://a.com/1"))
        repository.articlesToReturn = articles

        viewModel.getArticles("bbc")
        advanceUntilIdle()

        val state = viewModel.articlesUiState.value
        assertTrue(state is ArticlesUiState.Success)
        assertEquals(articles, (state as ArticlesUiState.Success).articles)
    }

    @Test
    fun `getArticles cancels previous job when called consecutively`() = runTest {
        val firstArticles = listOf(Article(title = "Headline 1", url = "https://a.com/1"))
        val secondArticles = listOf(Article(title = "Headline 2", url = "https://a.com/2"))

        repository.articlesToReturn = firstArticles
        viewModel.getArticles("source1")

        repository.articlesToReturn = secondArticles
        viewModel.getArticles("source2")

        advanceUntilIdle()

        val state = viewModel.articlesUiState.value
        assertTrue(state is ArticlesUiState.Success)
        assertEquals(secondArticles, (state as ArticlesUiState.Success).articles)
    }

    @Test
    fun `getArticles emits Error state when repository throws DataException`() = runTest {
        repository.shouldThrowException = true
        repository.exceptionToThrow = DataException(DataError.NoCache)

        viewModel.getArticles("bbc")
        advanceUntilIdle()

        val state = viewModel.articlesUiState.value
        assertTrue(state is ArticlesUiState.Error)
        assertEquals("No cached data available. Connect to the internet.", (state as ArticlesUiState.Error).message)
    }
}
