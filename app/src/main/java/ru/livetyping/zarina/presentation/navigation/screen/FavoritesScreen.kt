package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.FavoritesGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesScreen
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesScreenAction
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesViewModel

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
                val sizeSelectorResultFlow = it.savedStateHandle
                    .getStateFlow<SizeSelectorGraph.Result?>(
                        key = SizeSelectorGraph.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(sizeSelectorResultFlow)
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
