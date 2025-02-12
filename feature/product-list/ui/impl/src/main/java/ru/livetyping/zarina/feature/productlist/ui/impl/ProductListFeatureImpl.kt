package ru.livetyping.zarina.feature.productlist.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import ru.livetyping.zarina.core.deeplink.ZarinaWebLinkUris
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListScreen

public class ProductListFeatureImpl : ProductListFeature {
    override fun NavGraphBuilder.composable(
        actions: ProductListFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        composable<ProductListFeature.NavEntry>(
            typeMap = ProductListFeature.NavEntry.typeMap(),
            deepLinks = DeepLinks,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            ProductListScreen(navActions = actions)
        }
    }

    private companion object {
        private val DeepLinks = buildList {
            val categoryId = ProductListFeature.NavEntry.CATEGORY_ID_PROPERTY_NAME
            ZarinaWebLinkUris.forEach { uri ->
                // TODO: [Low] Migrate to navDeepLink<ProductListNavEntry>?
                add(navDeepLink { uriPattern = "$uri/catalog/product/{$categoryId}" })
                add(navDeepLink { uriPattern = "$uri/catalog/product/{$categoryId}/" })
            }
        }
    }
}
