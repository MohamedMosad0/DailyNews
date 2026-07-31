package com.mohamed.dailynews.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.dailynews.domain.usecase.GetArticlesUseCase
import com.mohamed.dailynews.domain.usecase.GetSourcesUseCase
import com.mohamed.dailynews.ui.screens.home.state.ArticlesUiState
import com.mohamed.dailynews.ui.screens.home.state.SourcesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getSourcesUseCase: GetSourcesUseCase,
    private val getArticlesUseCase: GetArticlesUseCase,
) : ViewModel() {

    private val _sourcesUiState = MutableStateFlow<SourcesUiState>(SourcesUiState.Loading)
    val sourcesUiState: StateFlow<SourcesUiState> = _sourcesUiState.asStateFlow()

    private val _articlesUiState = MutableStateFlow<ArticlesUiState>(ArticlesUiState.Initial)
    val articlesUiState: StateFlow<ArticlesUiState> = _articlesUiState.asStateFlow()

    private var getSourcesJob: Job? = null
    private var getArticlesJob: Job? = null

    fun getSources(category: String) {
        getSourcesJob?.cancel()
        getSourcesJob = viewModelScope.launch {
            _sourcesUiState.value = SourcesUiState.Loading
            try {
                val sourcesList = getSourcesUseCase.execute(category = category)
                _sourcesUiState.value = SourcesUiState.Success(sourcesList)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Timber.tag("getSources - onFailure").e("code = ${t.message}")
                _sourcesUiState.value = SourcesUiState.Error(
                    t.message ?: "Something went wrong please try again later"
                )
            }
        }
    }

    fun getArticles(source: String) {
        getArticlesJob?.cancel()
        getArticlesJob = viewModelScope.launch {
            _articlesUiState.value = ArticlesUiState.Loading
            try {
                val articlesList = getArticlesUseCase.execute(source = source)
                _articlesUiState.value = ArticlesUiState.Success(articlesList)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Timber.tag("getArticles - onFailure").e("body = $t")
                _articlesUiState.value = ArticlesUiState.Error(
                    t.message ?: "Something went wrong please try again later"
                )
            }
        }
    }
}