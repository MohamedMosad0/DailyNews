package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend fun execute(theme: AppTheme) = repository.setTheme(theme)
}
