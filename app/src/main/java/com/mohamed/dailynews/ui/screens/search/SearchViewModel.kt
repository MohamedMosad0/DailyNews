package com.mohamed.dailynews.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.usecase.SearchArticlesUseCase
import com.mohamed.dailynews.utils.error.DataError
import com.mohamed.dailynews.utils.error.DataException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

sealed interface SearchUiState {
    data object Initial : SearchUiState
    data object Loading : SearchUiState
    data class Success(val articles: List<Article>) : SearchUiState
    data object Empty : SearchUiState
    data class Error(val message: String) : SearchUiState
}

private const val MIN_QUERY_LENGTH = 3
private const val DEBOUNCE_TIMEOUT_MS = 300L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchArticlesUseCase: SearchArticlesUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<SearchUiState> = _searchQuery
        .debounce(DEBOUNCE_TIMEOUT_MS)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            flow {
                val trimmed = query.trim()
                if (trimmed.length < MIN_QUERY_LENGTH) {
                    emit(SearchUiState.Initial)
                } else {
                    emit(SearchUiState.Loading)
                    try {
                        Timber.tag("SearchViewModel").d("Executing searchArticlesUseCase with query='$trimmed'")
                        val articles = searchArticlesUseCase.execute(query = trimmed)
                        Timber.tag("SearchViewModel").d("Search succeeded with ${articles.size} articles")
                        if (articles.isEmpty()) {
                            emit(SearchUiState.Empty)
                        } else {
                            emit(SearchUiState.Success(articles))
                        }
                    } catch (t: Throwable) {
                        if (t is CancellationException) throw t
                        Timber.tag("SearchViewModel").e(t, "Search failed for query='$trimmed'")
                        val message = when (t) {
                            is DataException -> when (val error = t.error) {
                                is DataError.Offline -> "No internet connection"
                                is DataError.NoCache -> "No cached search results available."
                                is DataError.Timeout -> "Request timed out. Please try again."
                                is DataError.Server -> "Server error (${error.code}). Please try again later."
                                is DataError.Unknown -> "Failed to perform search. Please try again."
                            }
                            else -> t.message ?: "Failed to perform search. Please try again."
                        }
                        emit(SearchUiState.Error(message))
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState.Initial
        )

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearQuery() {
        _searchQuery.value = ""
    }
}
