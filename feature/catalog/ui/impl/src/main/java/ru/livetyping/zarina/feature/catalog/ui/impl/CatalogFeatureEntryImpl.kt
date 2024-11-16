package ru.livetyping.zarina.feature.catalog.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeatureEntry
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavEntry
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavParams
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.CatalogScreen

public class CatalogFeatureEntryImpl : CatalogFeatureEntry {
    override fun NavGraphBuilder.composable(
        actions: CatalogNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        composable<CatalogNavEntry> {
            CatalogScreen(navActions = actions)
        }
    }

    override fun getNavEntry(params: CatalogNavParams): CatalogNavEntry = CatalogNavEntry
}
