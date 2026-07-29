package com.mohamed.dailynews

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.ui.screens.SharedArticleViewModel
import com.mohamed.dailynews.ui.screens.detail.ArticleDetailScreen
import com.mohamed.dailynews.ui.screens.home.HomeScreen
import com.mohamed.dailynews.ui.screens.search.SearchScreen
import com.mohamed.dailynews.ui.screens.settings.SettingsViewModel
import com.mohamed.dailynews.ui.screens.splash.SplashScreen
import com.mohamed.dailynews.ui.theme.DailyNewsTheme
import com.mohamed.dailynews.ui.utils.ArticleDetailRoute
import com.mohamed.dailynews.ui.utils.HomeRoute
import com.mohamed.dailynews.ui.utils.SearchRoute
import com.mohamed.dailynews.ui.utils.SplashRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val currentTheme by settingsViewModel.theme.collectAsStateWithLifecycle()
            val currentLanguage by settingsViewModel.language.collectAsStateWithLifecycle()

            DailyNewsTheme(appTheme = currentTheme) {
                App(
                    currentTheme = currentTheme,
                    currentLanguage = currentLanguage,
                    onThemeChange = settingsViewModel::setTheme,
                    onLanguageChange = { newLanguage ->
                        settingsViewModel.setLanguage(newLanguage) {
                            recreate()
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun App(
    currentTheme: AppTheme,
    currentLanguage: AppLanguage,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
) {
    val navController = rememberNavController()
    val sharedViewModel: SharedArticleViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                currentTheme = currentTheme,
                currentLanguage = currentLanguage,
                onThemeChange = onThemeChange,
                onLanguageChange = onLanguageChange
            )
        }
        composable<SplashRoute> {
            SplashScreen(navController)
        }
        composable<SearchRoute> {
            SearchScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable<ArticleDetailRoute> {
            ArticleDetailScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}

@Composable
@Preview
fun AppPreview() {
    App(
        currentTheme = AppTheme.DARK,
        currentLanguage = AppLanguage.ENGLISH,
        onThemeChange = {},
        onLanguageChange = {},
    )
}
