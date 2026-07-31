package com.mohamed.dailynews.ui.screens.home.state

import com.mohamed.dailynews.domain.model.Article

sealed interface ArticlesUiState {
    data object Initial : ArticlesUiState
    data object Loading : ArticlesUiState
    data class Success(val articles: List<Article>) : ArticlesUiState
    data class Error(val message: String) : ArticlesUiState
}
