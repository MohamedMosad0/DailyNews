package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend fun execute(language: AppLanguage) = repository.setLanguage(language)
}
