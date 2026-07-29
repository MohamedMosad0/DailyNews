package com.mohamed.dailynews.domain.repository

import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getTheme(): Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
    fun getLanguage(): Flow<AppLanguage>
    suspend fun setLanguage(language: AppLanguage)
}
