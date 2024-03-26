package ru.zarina.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.screen.graph.navigateToSizeSelectorGraph
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
                        navController.navigateToFiltersScreen(
                            categoryId = action.categoryId,
                            filters = action.filters,
                        )
                    }

                    is ProductsScreenAction.TagClicked -> {
                        navController.navigateToProductsScreen(
                            categoryId = action.tag.id,
                            filters = action.filters,
                        )
                    }

                    is ProductsScreenAction.AddProductToCartClicked -> {
                        navController.navigateToSizeSelectorGraph(action.product)
                    }

                    is ProductsScreenAction.SubscribeToProductClicked -> {
                        if (action.product.offers.size > 1) {
                            navController.navigateToSizeSelectorGraph(action.product)
                        } else {
                            val offer = action.product.offers.firstOrNull() ?: return@ProductsScreen
                            navController.navigateToProductSubscriptionScreen(action.product, offer)
                        }
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToProductsScreen(
    categoryId: Category.Id,
    filters: Filters? = null,
) {
    val args = UnscopedDestinations.Products.Args(categoryId, filters)
    this.navigate(
        route = UnscopedDestinations.Products.routeSchema,
        args = UnscopedDestinations.Products.createArgsBundle(args),
    )
}
