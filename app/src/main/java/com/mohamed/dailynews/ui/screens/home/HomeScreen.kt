package com.mohamed.dailynews.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mohamed.dailynews.R
import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.ui.model.categories
import com.mohamed.dailynews.ui.screens.SharedArticleViewModel
import com.mohamed.dailynews.ui.screens.home.composables.DrawerContent
import com.mohamed.dailynews.ui.screens.home.composables.categories.CategoriesTab
import com.mohamed.dailynews.ui.screens.home.composables.news.ArticlePreviewBottomSheet
import com.mohamed.dailynews.ui.screens.home.composables.news.NewsTab
import com.mohamed.dailynews.ui.utils.ArticleDetailRoute
import com.mohamed.dailynews.ui.utils.SearchRoute
import kotlinx.coroutines.launch

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
    var selectedCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedCategory = remember(selectedCategoryId) { categories.find { it.id == selectedCategoryId } }

    BackHandler(enabled = selectedCategory != null) {
        selectedCategoryId = null
    }

    val selectedArticle by sharedViewModel.selectedArticle.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    // News state observation using StateFlow
    val sourcesUiState by newsViewModel.sourcesUiState.collectAsStateWithLifecycle()
    val articlesUiState by newsViewModel.articlesUiState.collectAsStateWithLifecycle()

    val layoutDirection = when (currentLanguage) {
        AppLanguage.ARABIC -> LayoutDirection.Rtl
        AppLanguage.ENGLISH -> LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    currentTheme = currentTheme,
                    currentLanguage = currentLanguage,
                    onThemeSelect = onThemeChange,
                    onLanguageSelect = onLanguageChange,
                    onGoToHomeClick = {
                        selectedCategoryId = null
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
                                    contentDescription = stringResource(id = R.string.cd_open_drawer),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        title = {
                            Text(
                                text = selectedCategory?.let { stringResource(id = it.titleResId) } ?: stringResource(R.string.home),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(SearchRoute) { launchSingleTop = true } }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(id = R.string.cd_search),
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
                            category = selectedCategory,
                            sourcesUiState = sourcesUiState,
                            articlesUiState = articlesUiState,
                            onLoadSources = { categoryId -> newsViewModel.getSources(categoryId) },
                            onLoadArticles = { sourceId -> newsViewModel.getArticles(sourceId) },
                            onArticleClick = { article ->
                                sharedViewModel.selectArticle(article)
                                showBottomSheet = true
                            }
                        )
                    } else {
                        CategoriesTab { category ->
                            selectedCategoryId = category.id
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
}