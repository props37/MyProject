package ru.livetyping.zarina.feature.product.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresNavEntry
import ru.livetyping.zarina.feature.product.ui.impl.impl.navigation.availabilityInStoresScreen
import ru.livetyping.zarina.feature.product.ui.impl.impl.navigation.productScreen
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.ProductNavActions

public class ProductFeatureImpl : ProductFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: ProductFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<ProductFeature.NavEntry>(
            startDestination = ProductFeature.NavEntry.StartNavEntry::class,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val productNavActions = ProductNavActions(
                onBackClicked = actions.onBackClicked,
                onCheckAvailabilityInStoresClicked = { product ->
                    val availabilityInStoresNavEntry = AvailabilityInStoresNavEntry.from(product)
                    navController.navigate(availabilityInStoresNavEntry)
                },
                onSubscribeToProductClicked = actions.onSubscribeToProductClicked,
                onProductClicked = actions.onProductClicked,
            )
            productScreen(productNavActions)

            val availabilityInStoresNavActions = AvailabilityInStoresNavActions(
                onBackClicked = { navController.navigateUp() },
            )
            availabilityInStoresScreen(availabilityInStoresNavActions)
        }
    }
}
