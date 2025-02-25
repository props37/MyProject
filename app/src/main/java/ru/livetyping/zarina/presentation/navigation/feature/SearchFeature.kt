package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature

fun NavGraphBuilder.searchFeature(
    feature: SearchFeature,
    actions: SearchFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}

@Composable
fun rememberSearchNavActions(
    navController: NavHostController
): SearchFeature.NavActions {
    return remember(navController) {
        SearchFeature.NavActions()
    }
}
