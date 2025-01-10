package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature

fun NavGraphBuilder.webViewFeature(
    feature: WebViewFeature,
    actions: WebViewFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberWebViewNavActions(
    navController: NavHostController
): WebViewFeature.NavActions {
    return remember(navController) {
        WebViewFeature.NavActions(
            onBackClicked = { navController.navigateUp() },
        )
    }
}
