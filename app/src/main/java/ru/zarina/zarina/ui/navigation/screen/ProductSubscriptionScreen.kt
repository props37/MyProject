package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductOffer
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.FavoritesGraph
import ru.zarina.zarina.ui.navigation.util.slideEnterTransition
import ru.zarina.zarina.ui.navigation.util.slidePopExitTransition
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreen
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenAction
import ru.zarina.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSubscription,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema,
                FavoritesGraph.Favorites.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema,
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
