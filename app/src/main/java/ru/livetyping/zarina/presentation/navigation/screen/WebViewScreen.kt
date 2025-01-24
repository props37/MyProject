package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.webview.WebViewScreen
import ru.livetyping.zarina.presentation.screen.webview.WebViewScreenAction

fun NavGraphBuilder.webViewScreen(navController: NavHostController) {
    composable<UnscopedDestinations.WebView>(
        enterTransition = { slideEnterTransition() },
        exitTransition = { slideExitTransition() },
        popEnterTransition = { slidePopEnterTransition() },
        popExitTransition = { slidePopExitTransition() },
    ) {
        WebViewScreen(
            navigate = { action ->
                when (action) {
                    WebViewScreenAction.BackClicked -> navController.navigateUp()
                }
            },
        )
    }
}
