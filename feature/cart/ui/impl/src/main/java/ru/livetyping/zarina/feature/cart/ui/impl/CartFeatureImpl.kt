package ru.livetyping.zarina.feature.cart.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.api.CartNavActions
import ru.livetyping.zarina.feature.cart.ui.api.CartNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.cartScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavActions as CartScreenNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavEntry as CartScreenNavEntry

public class CartFeatureImpl : CartFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: CartNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<CartNavEntry>(
            startDestination = CartScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val cartScreenNavActions = CartScreenNavActions(
                onBackClicked = actions.onBackClicked,
            )
            cartScreen(cartScreenNavActions)
        }
    }
}
