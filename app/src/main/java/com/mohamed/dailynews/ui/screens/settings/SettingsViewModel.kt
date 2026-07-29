package com.mohamed.dailynews.ui.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.domain.usecase.GetLanguageUseCase
import com.mohamed.dailynews.domain.usecase.GetThemeUseCase
import com.mohamed.dailynews.domain.usecase.SetLanguageUseCase
import com.mohamed.dailynews.domain.usecase.SetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
) : ViewModel() {

    val theme: StateFlow<AppTheme> = getThemeUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.DARK
        )

    val language: StateFlow<AppLanguage> = getLanguageUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppLanguage.ENGLISH
        )

    fun setTheme(newTheme: AppTheme) {
        viewModelScope.launch {
            setThemeUseCase.execute(newTheme)
        }
    }

    fun setLanguage(newLanguage: AppLanguage) {
        viewModelScope.launch {
            setLanguageUseCase.execute(newLanguage)
            val appLocales = LocaleListCompat.forLanguageTags(newLanguage.code)
            AppCompatDelegate.setApplicationLocales(appLocales)
        }
    }
}
