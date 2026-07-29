package com.mohamed.dailynews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.mohamed.dailynews.domain.model.AppLanguage
import com.mohamed.dailynews.domain.model.AppTheme
import com.mohamed.dailynews.ui.screens.SharedArticleViewModel
import com.mohamed.dailynews.ui.screens.detail.ArticleDetailScreen
import com.mohamed.dailynews.ui.screens.home.HomeScreen
import com.mohamed.dailynews.ui.screens.maps.MapScreenWrapper
import com.mohamed.dailynews.ui.screens.search.SearchScreen
import com.mohamed.dailynews.ui.screens.settings.SettingsViewModel
import com.mohamed.dailynews.ui.screens.splash.SplashScreen
import com.mohamed.dailynews.ui.theme.DailyNewsTheme
import com.mohamed.dailynews.ui.utils.ArticleDetailRoute
import com.mohamed.dailynews.ui.utils.HomeRoute
import com.mohamed.dailynews.ui.utils.MapRoute
import com.mohamed.dailynews.ui.utils.SearchRoute
import com.mohamed.dailynews.ui.utils.SplashRoute
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val currentTheme by settingsViewModel.theme.collectAsStateWithLifecycle()
            val currentLanguage by settingsViewModel.language.collectAsStateWithLifecycle()

            val context = LocalContext.current
            val configuration = LocalConfiguration.current

            LaunchedEffect(currentLanguage) {
                val locale = Locale.forLanguageTag(currentLanguage.code)
                Locale.setDefault(locale)
                configuration.setLocale(locale)
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
            }

            DailyNewsTheme(appTheme = currentTheme) {
                App(
                    currentTheme = currentTheme,
                    currentLanguage = currentLanguage,
                    onThemeChange = settingsViewModel::setTheme,
                    onLanguageChange = settingsViewModel::setLanguage,
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
        composable<MapRoute> {
            MapScreenWrapper()
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
