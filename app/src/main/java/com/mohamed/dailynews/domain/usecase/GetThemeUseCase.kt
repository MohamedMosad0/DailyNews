package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    fun execute(): Flow<AppTheme> = repository.getTheme()
}
