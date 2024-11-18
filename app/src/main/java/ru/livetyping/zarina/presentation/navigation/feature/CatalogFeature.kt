package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.presentation.feature.Features

fun NavGraphBuilder.catalogFeature(
    feature: CatalogFeature,
    actions: CatalogNavActions,
) {
    with(feature) {
        composable(actions)
    }
}

@Composable
fun rememberCatalogNavActions(
    features: Features,
    navController: NavHostController
): CatalogNavActions {
    return remember(features, navController) {
        CatalogNavActions()
    }
}
