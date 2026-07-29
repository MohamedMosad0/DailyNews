package com.mohamed.dailynews.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mohamed.dailynews.R
import com.mohamed.dailynews.data.api.model.ArticleDM
import com.mohamed.dailynews.ui.model.Category
import com.mohamed.dailynews.ui.screens.home.composables.DrawerContent
import com.mohamed.dailynews.ui.screens.home.composables.categories.CategoriesTab
import com.mohamed.dailynews.ui.screens.home.composables.news.NewsTab
import kotlinx.coroutines.launch

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.livedata.observeAsState
import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.ui.screens.SharedArticleViewModel
import com.mohamed.dailynews.ui.screens.home.composables.news.ArticlePreviewBottomSheet
import com.mohamed.dailynews.ui.utils.ArticleDetailRoute
import com.mohamed.dailynews.ui.utils.SearchRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedArticleViewModel = hiltViewModel(),
    newsViewModel: NewsViewModel = hiltViewModel(),
    currentTheme: AppTheme,
    currentLanguage: AppLanguage,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val selectedArticle by sharedViewModel.selectedArticle.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    // News state observation
    val newsTabs by newsViewModel.tabs.observeAsState()
    val isNewsLoading by newsViewModel.isLoading.observeAsState(false)
    val newsErrorMessage by newsViewModel.errorMessage.observeAsState()
    
    val articles by newsViewModel.articles.observeAsState()
    val isArticlesLoading by newsViewModel.isLoadingArticles.observeAsState(false)
    val articlesErrorMessage by newsViewModel.articlesErrorMessage.observeAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentTheme = currentTheme,
                currentLanguage = currentLanguage,
                onThemeSelect = onThemeChange,
                onLanguageSelect = onLanguageChange,
                onGoToHomeClick = {
                    selectedCategory = null
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Drawer",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    title = {
                        Text(
                            text = selectedCategory?.title ?: stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(SearchRoute) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                if (selectedCategory != null) {
                    NewsTab(
                        category = selectedCategory!!,
                        sources = newsTabs,
                        isSourcesLoading = isNewsLoading,
                        sourcesErrorMessage = newsErrorMessage,
                        articles = articles,
                        isArticlesLoading = isArticlesLoading,
                        articlesErrorMessage = articlesErrorMessage,
                        onLoadSources = { categoryId: String -> newsViewModel.getSources(categoryId) },
                        onLoadArticles = { sourceId: String -> newsViewModel.getArticles(sourceId) },
                        onArticleClick = { article ->
                            sharedViewModel.selectArticle(article)
                            showBottomSheet = true
                        }
                    )
                } else {
                    CategoriesTab { category ->
                        selectedCategory = category
                    }
                }
            }

            if (showBottomSheet && selectedArticle != null) {
                ArticlePreviewBottomSheet(
                    article = selectedArticle,
                    onDismissRequest = { showBottomSheet = false },
                    onViewFullArticleClick = {
                        showBottomSheet = false
                        navController.navigate(ArticleDetailRoute)
                    }
                )
            }
        }
    }
}