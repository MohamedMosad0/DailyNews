package com.mohamed.dailynews.ui.screens.home.composables.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohamed.dailynews.R
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.ui.composables.DefaultErrorMessage
import com.mohamed.dailynews.ui.composables.DefaultLoadingView
import com.mohamed.dailynews.ui.model.Category
import com.mohamed.dailynews.ui.screens.home.state.ArticlesUiState
import com.mohamed.dailynews.ui.screens.home.state.SourcesUiState

@Composable
fun NewsTab(
    category: Category,
    sourcesUiState: SourcesUiState,
    articlesUiState: ArticlesUiState,
    onLoadSources: (String) -> Unit,
    onLoadArticles: (String) -> Unit,
    onArticleClick: (Article) -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(category.id) {
        selectedTabIndex = 0
        onLoadSources(category.id)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (sourcesUiState) {
            is SourcesUiState.Loading -> {
                DefaultLoadingView()
            }
            is SourcesUiState.Error -> {
                DefaultErrorMessage(sourcesUiState.message) {
                    onLoadSources(category.id)
                }
            }
            is SourcesUiState.Success -> {
                val sources = sourcesUiState.sources
                if (sources.isNotEmpty()) {
                    val safeIndex = selectedTabIndex.coerceIn(0, sources.size - 1)

                    ScrollableTabRow(
                        selectedTabIndex = safeIndex,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        edgePadding = 12.dp,
                        indicator = { tabPositions ->
                            if (safeIndex < tabPositions.size) {
                                Box(
                                    Modifier
                                        .tabIndicatorOffset(tabPositions[safeIndex])
                                        .padding(horizontal = 8.dp)
                                        .height(2.dp)
                                        .background(color = MaterialTheme.colorScheme.onBackground)
                                )
                            }
                        },
                        divider = {}
                    ) {
                        sources.forEachIndexed { index, source ->
                            val isSelected = safeIndex == index
                            Tab(
                                selected = isSelected,
                                onClick = { selectedTabIndex = index },
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                            ) {
                                Text(
                                    text = source.name ?: "",
                                    style = if (isSelected) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }
                    }

                    ArticlesList(
                        source = sources[safeIndex].id,
                        articlesUiState = articlesUiState,
                        onLoadArticles = onLoadArticles,
                        onArticleClick = onArticleClick
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_sources),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
