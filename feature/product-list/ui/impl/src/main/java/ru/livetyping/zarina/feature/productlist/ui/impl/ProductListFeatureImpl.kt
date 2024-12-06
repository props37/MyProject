package ru.livetyping.zarina.feature.productlist.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.ProductListScreen

public class ProductListFeatureImpl : ProductListFeature {
    override fun NavGraphBuilder.composable(
        actions: ProductListNavActions,
        resultRetrievers: Unit,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        composable<ProductListNavEntry>(
            typeMap = ProductListNavEntry.typeMap(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            ProductListScreen(navActions = actions)
        }
    }
}
