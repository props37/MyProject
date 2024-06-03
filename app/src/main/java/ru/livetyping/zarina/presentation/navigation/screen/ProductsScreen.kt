package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CatalogGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSizeSelectorGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.products.ProductsScreen
import ru.livetyping.zarina.presentation.screen.products.ProductsScreenAction
import ru.livetyping.zarina.presentation.screen.products.ProductsViewModel
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productsScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Products,
        enterTransition = {
            when (initialState.destination.route) {
                CatalogGraph.Catalog.routeSchema,
                UnscopedDestinations.ProductSearch.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ProductFilters.routeSchema,
                UnscopedDestinations.ProductSubscription.routeSchema,
                UnscopedDestinations.Product.routeSchema,
                UnscopedDestinations.ProductSearch.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ProductFilters.routeSchema,
                UnscopedDestinations.ProductSubscription.routeSchema,
                UnscopedDestinations.Product.routeSchema,
                UnscopedDestinations.ProductSearch.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                CatalogGraph.Catalog.routeSchema,
                UnscopedDestinations.ProductSearch.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        ProductsScreen(
            viewModel = hiltViewModel { factory: ProductsViewModel.Factory ->
                val filtersResultFlow = it.savedStateHandle
                    .getStateFlow<UnscopedDestinations.ProductFilters.Result?>(
                        key = UnscopedDestinations.ProductFilters.RESULT_KEY,
                        initialValue = null,
                    )
                val sizeSelectorResultFlow = it.savedStateHandle
                    .getStateFlow<SizeSelectorGraph.Result?>(
                        key = SizeSelectorGraph.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(filtersResultFlow, sizeSelectorResultFlow)
            },
            navigate = { action ->
                when (action) {
                    ProductsScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Products.routeSchema,
                            inclusive = true,
                        )
                    }

                    ProductsScreenAction.SearchClicked -> {
                        navController.navigateToProductSearchScreen()
                    }

                    is ProductsScreenAction.FiltersClicked -> {
                        navController.navigateToProductFiltersScreen(
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

                    is ProductsScreenAction.ProductClicked -> {
                        navController.navigateToProductScreen(action.product.id)
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
