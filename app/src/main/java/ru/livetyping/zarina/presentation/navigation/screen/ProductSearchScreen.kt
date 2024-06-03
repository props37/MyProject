package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CatalogGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.util.fadeInTransition
import ru.livetyping.zarina.presentation.navigation.util.fadeOutTransition
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreen
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenAction
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel

fun NavGraphBuilder.productSearchScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSearch,
        enterTransition = {
            when (initialState.destination.route) {
                CatalogGraph.Catalog.routeSchema -> fadeInTransition()
                UnscopedDestinations.Products.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema,
                UnscopedDestinations.ProductSubscription.routeSchema,
                UnscopedDestinations.Product.routeSchema,
                UnscopedDestinations.ProductSearchFilters.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema,
                UnscopedDestinations.ProductSubscription.routeSchema,
                UnscopedDestinations.Product.routeSchema,
                UnscopedDestinations.ProductSearchFilters.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                CatalogGraph.Catalog.routeSchema -> fadeOutTransition()
                UnscopedDestinations.Products.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ProductSearchScreen(
            viewModel = hiltViewModel { factory: ProductSearchViewModel.Factory ->
                val sizeSelectorResultFlow = it.savedStateHandle
                    .getStateFlow<SizeSelectorGraph.Result?>(
                        key = SizeSelectorGraph.RESULT_KEY,
                        initialValue = null,
                    )
                val filtersResultFlow = it.savedStateHandle
                    .getStateFlow<UnscopedDestinations.ProductSearchFilters.Result?>(
                        key = UnscopedDestinations.ProductSearchFilters.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(sizeSelectorResultFlow, filtersResultFlow)
            },
            navigate = { action ->
                when (action) {
                    ProductSearchScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSearch.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ProductSearchScreenAction.CategoryClicked -> {
                        navController.navigateToProductsScreen(action.categoryId)
                    }

                    is ProductSearchScreenAction.ProductClicked -> {
                        navController.navigateToProductScreen(action.product.id)
                    }

                    is ProductSearchScreenAction.AddProductToCartClicked -> {
                        navController.navigateToSizeSelectorGraph(action.product)
                    }

                    is ProductSearchScreenAction.SubscribeToProductClicked -> {
                        if (action.product.offers.size > 1) {
                            navController.navigateToSizeSelectorGraph(action.product)
                        } else {
                            val offer = action.product.offers.firstOrNull() ?: return@ProductSearchScreen
                            navController.navigateToProductSubscriptionScreen(action.product, offer)
                        }
                    }

                    is ProductSearchScreenAction.FiltersClicked -> {
                        navController.navigateToProductSearchFiltersScreen(
                            searchQuery = action.searchQuery,
                            filters = action.filters,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToProductSearchScreen() {
    this.navigate(UnscopedDestinations.ProductSearch.route)
}
