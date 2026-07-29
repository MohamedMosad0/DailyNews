package com.mohamed.dailynews.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.mohamed.dailynews.R
import com.mohamed.dailynews.ui.screens.SharedArticleViewModel
import com.mohamed.dailynews.ui.theme.DailyNewsShapes
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ArticleDetailScreen(
    navController: NavController,
    sharedViewModel: SharedArticleViewModel
) {
    val article by sharedViewModel.selectedArticle.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var navigatingBack by remember { mutableStateOf(false) }

    val cleanSourceTitle = article?.sourceName?.trim()?.takeIf {
        it.isNotEmpty() && !it.equals("null", ignoreCase = true) && !it.equals("unknown", ignoreCase = true)
    } ?: stringResource(R.string.app_name)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { 
                        if (!navigatingBack) {
                            navigatingBack = true
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                title = {
                    Text(
                        text = cleanSourceTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        if (article == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_article_selected),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            val currentArticle = article!!
            val scrollState = rememberScrollState()

            val authorText = formatAuthorText(currentArticle.author, currentArticle.sourceName)
            val publishedDateText = formatPublishedAtDate(currentArticle.publishedAt)
            val bodyContentText = cleanContentText(currentArticle.content, currentArticle.description)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Article Hero Image
                GlideImage(
                    model = currentArticle.urlToImage,
                    contentDescription = currentArticle.title,
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = { it.timeout(30000) },
                    modifier = Modifier
                        .height(230.dp)
                        .fillMaxWidth()
                        .clip(DailyNewsShapes.large),
                    loading = placeholder(R.drawable.ic_dailynews_logo),
                    failure = placeholder(R.drawable.ic_dailynews_logo)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Headline
                Text(
                    text = currentArticle.title ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Byline & Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = authorText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Text(
                        text = publishedDateText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Article Content Body
                Text(
                    text = bodyContentText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Fallback & External Web Link Button
                Button(
                    onClick = {
                        val webUrl = currentArticle.url
                        if (!webUrl.isNullOrEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                            context.startActivity(intent)
                        }
                    },
                    shape = DailyNewsShapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.read_full_on_web),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun cleanContentText(content: String?, description: String?): String {
    val rawText = content?.takeIf { it.isNotBlank() } ?: description ?: ""
    return rawText.replace(Regex("""\s*\[\+\d+\s*chars\]""", RegexOption.IGNORE_CASE), "").trim()
}

private fun formatPublishedAtDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val parsedDate = inputFormat.parse(rawDate)
        if (parsedDate != null) {
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            outputFormat.format(parsedDate)
        } else {
            rawDate.split("T").firstOrNull() ?: rawDate
        }
    } catch (e: Exception) {
        rawDate.split("T").firstOrNull() ?: rawDate
    }
}

@Composable
private fun formatAuthorText(author: String?, sourceName: String?): String {
    val cleanAuthor = author?.trim()?.takeIf {
        it.isNotEmpty() && !it.equals("null", ignoreCase = true) && !it.equals("unknown", ignoreCase = true)
    }
    val cleanSource = sourceName?.trim()?.takeIf {
        it.isNotEmpty() && !it.equals("null", ignoreCase = true) && !it.equals("unknown", ignoreCase = true)
    }
    val displayAuthor = cleanAuthor ?: cleanSource
    return if (!displayAuthor.isNullOrEmpty()) stringResource(R.string.by_author, displayAuthor) else ""
}
