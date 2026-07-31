package com.mohamed.dailynews.ui.screens.home.state

import com.mohamed.dailynews.domain.model.Source

sealed interface SourcesUiState {
    data object Loading : SourcesUiState
    data class Success(val sources: List<Source>) : SourcesUiState
    data class Error(val message: String) : SourcesUiState
}
