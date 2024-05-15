package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.FavoritesGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.product.ProductScreen
import ru.livetyping.zarina.presentation.screen.product.ProductScreenAction
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Product,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Product.routeSchema,
                UnscopedDestinations.Products.routeSchema,
                FavoritesGraph.Favorites.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Product.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Product.routeSchema -> slidePopEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Product.routeSchema,
                UnscopedDestinations.Products.routeSchema,
                FavoritesGraph.Favorites.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        ProductScreen(
            viewModel = hiltViewModel { factory: ProductViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    ProductScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Product.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ProductScreenAction.ProductClicked -> {
                        navController.navigateToProductScreen(action.product.id)
                    }

                    is ProductScreenAction.AddProductToCartClicked -> {
                        navController.navigateToSizeSelectorGraph(action.product)
                    }

                    is ProductScreenAction.SubscribeToProductClicked -> {
                        if (action.product.offers.size > 1) {
                            navController.navigateToSizeSelectorGraph(action.product)
                        } else {
                            val offer = action.product.offers.firstOrNull() ?: return@ProductScreen
                            navController.navigateToProductSubscriptionScreen(action.product, offer)
                        }
                    }
                }
            }
        )
    }
}

fun NavHostController.navigateToProductScreen(productId: Product.Id) {
    val args = UnscopedDestinations.Product.Args(productId)
    this.navigate(
        route = UnscopedDestinations.Product.routeSchema,
        args = UnscopedDestinations.Product.createArgsBundle(args),
    )
}
