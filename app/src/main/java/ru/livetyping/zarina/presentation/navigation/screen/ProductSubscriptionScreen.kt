package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.FavoritesGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.productsubscription.ProductSubscriptionScreen
import ru.livetyping.zarina.presentation.screen.productsubscription.ProductSubscriptionScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSubscription,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema,
                UnscopedDestinations.ProductSearch.routeSchema,
                FavoritesGraph.Favorites.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema,
                UnscopedDestinations.ProductSearch.routeSchema,
                FavoritesGraph.Favorites.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        ProductSubscriptionScreen(
            navigate = { action ->
                when (action) {
                    ProductSubscriptionScreenAction.ScreenClosed,
                    ProductSubscriptionScreenAction.SubscriptionCompleted -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSubscription.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToProductSubscriptionScreen(
    product: Product,
    offer: ProductOffer,
    navOptions: NavOptions? = null,
) {
    val args = UnscopedDestinations.ProductSubscription.Args(product, offer)
    this.navigate(
        route = UnscopedDestinations.ProductSubscription.routeSchema,
        args = UnscopedDestinations.ProductSubscription.createArgsBundle(args),
        navOptions = navOptions,
    )
}
