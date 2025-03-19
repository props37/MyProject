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
import ru.livetyping.zarina.feature.cart.ui.impl.impl.customer.RecipientNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.customer.RecipientNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.cartScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.recipientScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavActions as CartScreenNavActions

public class CartFeatureImpl : CartFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: CartFeature.NavActions,
        resultRetrievers: CartFeature.NavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<CartFeature.NavEntry>(
            startDestination = CartFeature.NavEntry.StartNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val cartScreenNavActions = CartScreenNavActions(
                onBackClicked = actions.onBackClicked,
                onChangeCityClicked = actions.onChangeCityClicked,
                onGoToCatalogClicked = actions.onGoToCatalogClicked,
                onProductClicked = actions.onProductClicked,
                onCheckoutClicked = { cartType ->
                    val navEntry = RecipientNavEntry.from(cartType)
                    navController.navigate(navEntry)
                },
            )
            cartScreen(
                actions = cartScreenNavActions,
                selectedCityResultRetriever = resultRetrievers.selectedCityResultRetriever,
            )

            val recipientNavActions = RecipientNavActions(
                onCloseClicked = {
                    navController.popBackStack<CartFeature.NavEntry.StartNavEntry>(inclusive = false)
                },
            )
            recipientScreen(recipientNavActions)
        }
    }
}
