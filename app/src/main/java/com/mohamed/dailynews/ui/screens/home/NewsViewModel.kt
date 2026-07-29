package com.mohamed.dailynews.ui.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.domain.model.Source
import com.mohamed.dailynews.domain.usecase.GetArticlesUseCase
import com.mohamed.dailynews.domain.usecase.GetSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getSourcesUseCase: GetSourcesUseCase,
    private val getArticlesUseCase: GetArticlesUseCase,
) : ViewModel() {

    val tabs: MutableLiveData<List<Source>?> = MutableLiveData(null)
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoadingArticles: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val articlesErrorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val articles: MutableLiveData<List<Article>?> = MutableLiveData(null)

    fun getSources(category: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                tabs.value = getSourcesUseCase.execute(category = category)
                isLoading.value = false
            } catch (t: Throwable) {
                isLoading.value = false
                Timber.tag("getSources - onFailure").e("code = ${t.message}")
                errorMessage.value = t.message ?: "Something went wrong please try again later"
            }
        }
    }

    fun getArticles(source: String) {
        viewModelScope.launch {
            try {
                isLoadingArticles.value = true
                val result = getArticlesUseCase.execute(source = source)
                isLoadingArticles.value = false
                articles.value = result
            } catch (t: Throwable) {
                Timber.tag("getArticles - onFailure").e("body = $t")
                isLoadingArticles.value = false
                articlesErrorMessage.value =
                    t.message ?: "Something went wrong please try again later"
            }
        }
    }
}