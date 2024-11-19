package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasAnyRoute
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitFadeOutTransition
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItemNavEntryClasses
import ru.livetyping.zarina.presentation.feature.Features

fun NavGraphBuilder.catalogFeature(
    feature: CatalogFeature,
    actions: CatalogNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                val initialDestination = initialState.destination
                when {
                    initialDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            exitTransition = {
                val targetDestination = targetState.destination
                when {
                    targetDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val initialDestination = initialState.destination
                when {
                    initialDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val targetDestination = targetState.destination
                when {
                    targetDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
        )
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
