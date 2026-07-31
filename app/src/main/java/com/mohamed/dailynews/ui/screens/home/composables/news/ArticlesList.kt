package com.mohamed.dailynews.ui.screens.home.composables.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.mohamed.dailynews.R
import com.mohamed.dailynews.domain.model.Article
import com.mohamed.dailynews.ui.composables.DefaultErrorMessage
import com.mohamed.dailynews.ui.composables.DefaultLoadingView
import com.mohamed.dailynews.ui.screens.home.state.ArticlesUiState
import com.mohamed.dailynews.ui.theme.DailyNewsShapes

@Composable
fun ArticlesList(
    source: String,
    articlesUiState: ArticlesUiState,
    onLoadArticles: (String) -> Unit,
    onArticleClick: (Article) -> Unit = {}
) {
    LaunchedEffect(source) {
        onLoadArticles(source)
    }

    when (articlesUiState) {
        is ArticlesUiState.Initial, is ArticlesUiState.Loading -> {
            DefaultLoadingView()
        }
        is ArticlesUiState.Error -> {
            DefaultErrorMessage(message = articlesUiState.message) {
                onLoadArticles(source)
            }
        }
        is ArticlesUiState.Success -> {
            val articles = articlesUiState.articles
            if (articles.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {
                    items(articles, key = { article -> article.url ?: article.hashCode() }) { article ->
                        ArticleItem(article = article, onClick = { onArticleClick(article) })
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_articles),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleItem(article: Article, onClick: () -> Unit = {}) {
    Card(
        shape = DailyNewsShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            GlideImage(
                model = article.urlToImage,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                requestBuilderTransform = { it.timeout(30000) },
                modifier = Modifier
                    .height(190.dp)
                    .fillMaxWidth()
                    .clip(DailyNewsShapes.medium),
                loading = placeholder(R.drawable.ic_dailynews_logo),
                failure = placeholder(R.drawable.ic_dailynews_logo)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = article.title ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.author ?: article.sourceName ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Text(
                    text = article.publishedAt ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
