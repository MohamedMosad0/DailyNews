package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    fun execute(): Flow<AppLanguage> = repository.getLanguage()
}
