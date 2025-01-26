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
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.CatalogScreen

public class CatalogFeatureImpl : CatalogFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: CatalogFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<CatalogFeature.NavEntry>(
            startDestination = CatalogFeature.NavEntry.StartNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            composable<CatalogFeature.NavEntry.StartNavEntry>(deepLinks = DeepLinks) {
                CatalogScreen(navActions = actions)
            }
        }
    }

    private companion object {
        private val DeepLinks = buildList {
            // TODO: [Low] Migrate to navDeepLink<CatalogNavEntry>?
            ZarinaWebLinkUris.forEach { uri ->
                add(navDeepLink { uriPattern = "$uri/catalog" })
                add(navDeepLink { uriPattern = "$uri/catalog/" })
            }
        }
    }
}
