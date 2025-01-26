package ru.livetyping.zarina.feature.productsubscription.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavActions
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavEntry
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.ProductSubscriptionScreen

public class ProductSubscriptionFeatureImpl : ProductSubscriptionFeature {
    override fun NavGraphBuilder.composable(
        actions: ProductSubscriptionNavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        composable<ProductSubscriptionNavEntry>(
            typeMap = ProductSubscriptionNavEntry.typeMap(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            ProductSubscriptionScreen(actions)
        }
    }
}
