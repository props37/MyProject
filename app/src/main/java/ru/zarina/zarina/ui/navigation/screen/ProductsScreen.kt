package ru.zarina.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.navigation.util.slideExitTransition
import ru.zarina.zarina.ui.navigation.util.slidePopEnterTransition
import ru.zarina.zarina.ui.screen.products.ProductsScreen
import ru.zarina.zarina.ui.screen.products.ProductsScreenAction
import ru.zarina.zarina.ui.screen.products.ProductsViewModel
import ru.zarina.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productsScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Products,
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Filters.routeSchema,
                UnscopedDestinations.ProductSubscription.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Filters.routeSchema,
                UnscopedDestinations.ProductSubscription.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
    ) {
        ProductsScreen(
            viewModel = hiltViewModel { factory: ProductsViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    ProductsScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Products.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ProductsScreenAction.FiltersClicked -> {
                        val args = UnscopedDestinations.Filters.Args(
                            categoryId = action.categoryId,
                            filters = action.filters,
                        )
                        navController.navigate(
                            route = UnscopedDestinations.Filters.routeSchema,
                            args = UnscopedDestinations.Filters.createArgsBundle(args),
                        )
                    }

                    is ProductsScreenAction.TagClicked -> {
                        val args = UnscopedDestinations.Products.Args(
                            categoryId = action.tag.id,
                            filters = action.filters,
                        )
                        navController.navigate(
                            route = UnscopedDestinations.Products.routeSchema,
                            args = UnscopedDestinations.Products.createArgsBundle(args),
                        )
                    }

                    is ProductsScreenAction.AddProductToCartClicked -> {
                        val args = SizeSelectorGraph.SizeSelector.Args(action.product)
                        navController.navigate(
                            route = SizeSelectorGraph.routeSchema,
                            args = SizeSelectorGraph.createArgsBundle(args),
                        )
                    }

                    is ProductsScreenAction.SubscribeToProductClicked -> {
                        if (action.product.offers.size > 1) {
                            val args = SizeSelectorGraph.SizeSelector.Args(action.product)
                            navController.navigate(
                                route = SizeSelectorGraph.routeSchema,
                                args = SizeSelectorGraph.createArgsBundle(args),
                            )
                        } else {
                            val offer = action.product.offers.firstOrNull() ?: return@ProductsScreen
                            val args = UnscopedDestinations.ProductSubscription.Args(
                                product = action.product,
                                offer = offer,
                            )
                            navController.navigate(
                                route = UnscopedDestinations.ProductSubscription.routeSchema,
                                args = UnscopedDestinations.ProductSubscription.createArgsBundle(args),
                            )
                        }
                    }
                }
            },
        )
    }
}
