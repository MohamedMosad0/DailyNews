package com.mohamed.dailynews.ui.screens.search

import com.mohamed.dailynews.data.fakes.FakeNewsRepository
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.usecase.SearchArticlesUseCase
import com.mohamed.dailynews.utils.error.DataError
import com.mohamed.dailynews.utils.error.DataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeNewsRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeNewsRepository()
        viewModel = SearchViewModel(
            searchArticlesUseCase = SearchArticlesUseCase(repository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is SearchUiState Initial`() = runTest {
        assertEquals(SearchUiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `query shorter than 3 characters keeps uiState at Initial`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onQueryChanged("ab")
        testScheduler.advanceTimeBy(350L)
        advanceUntilIdle()

        assertEquals(SearchUiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `valid query with search results emits Success state`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        val articles = listOf(Article(title = "Kotlin News", url = "https://a.com/1"))
        repository.searchArticlesToReturn = articles

        viewModel.onQueryChanged("Kotlin")
        testScheduler.advanceTimeBy(350L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected Success state but was $state", state is SearchUiState.Success)
        assertEquals(articles, (state as SearchUiState.Success).articles)
    }

    @Test
    fun `valid query with empty search results emits Empty state`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        repository.searchArticlesToReturn = emptyList()

        viewModel.onQueryChanged("NonExistentQuery")
        testScheduler.advanceTimeBy(350L)
        advanceUntilIdle()

        assertEquals(SearchUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `search error emits Error state with formatted message`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        repository.shouldThrowException = true
        repository.exceptionToThrow = DataException(DataError.Offline)

        viewModel.onQueryChanged("Android")
        testScheduler.advanceTimeBy(350L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected Error state but was $state", state is SearchUiState.Error)
        assertEquals("No internet connection", (state as SearchUiState.Error).message)
    }

    @Test
    fun `clearQuery resets searchQuery to empty string`() = runTest {
        viewModel.onQueryChanged("Search Term")
        viewModel.clearQuery()

        assertEquals("", viewModel.searchQuery.value)
    }
}
