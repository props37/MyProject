package ru.livetyping.zarina.feature.catalog.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import ru.livetyping.zarina.core.deeplink.ZarinaWebLinkUris
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavEntry
import ru.livetyping.zarina.feature.catalog.ui.CatalogScreenNavEntry
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.CatalogScreen

public class CatalogFeatureImpl : CatalogFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: CatalogNavActions,
        resultRetrievers: Unit,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<CatalogNavEntry>(
            startDestination = CatalogScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            composable<CatalogScreenNavEntry>(deepLinks = DeepLinks) {
                CatalogScreen(navActions = actions)
            }
        }
    }

    private companion object {
        private val DeepLinks = buildList {
            // TODO: [High] Migrate to navDeepLink<CatalogNavEntry>?
            ZarinaWebLinkUris.forEach { uri ->
                add(navDeepLink { uriPattern = "$uri/catalog" })
                add(navDeepLink { uriPattern = "$uri/catalog/" })
            }
        }
    }
}
