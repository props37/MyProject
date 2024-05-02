package ru.livetyping.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.navigation.destination.graph.FavoritesGraph
import ru.livetyping.zarina.ui.navigation.screen.graph.navigateToSizeSelectorGraph
import ru.livetyping.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.ui.navigation.util.slideExitTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.ui.screen.favorites.FavoritesScreen
import ru.livetyping.zarina.ui.screen.favorites.FavoritesScreenAction
import ru.livetyping.zarina.ui.screen.favorites.FavoritesViewModel

fun NavGraphBuilder.favoritesScreen(navController: NavHostController) {
    composableDestination(
        destination = FavoritesGraph.Favorites,
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ProductSubscription.routeSchema,
                UnscopedDestinations.Product.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ProductSubscription.routeSchema,
                UnscopedDestinations.Product.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
    ) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        FavoritesScreen(
            viewModel = hiltViewModel { factory: FavoritesViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    FavoritesScreenAction.GoToCatalogClicked -> {
                        navController.navigateToCatalogScreen()
                    }

                    is FavoritesScreenAction.AddProductToCartClicked -> {
                        navController.navigateToSizeSelectorGraph(action.product)
                    }

                    is FavoritesScreenAction.SubscribeToProductClicked -> {
                        if (action.product.offers.size > 1) {
                            navController.navigateToSizeSelectorGraph(action.product)
                        } else {
                            val offer = action.product.offers.firstOrNull() ?: return@FavoritesScreen
                            navController.navigateToProductSubscriptionScreen(action.product, offer)
                        }
                    }

                    is FavoritesScreenAction.ProductClicked -> {
                        navController.navigateToProductScreen(action.product.id)
                    }
                }
            },
        )
    }
}
