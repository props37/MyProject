package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CatalogGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreen
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenAction

fun NavGraphBuilder.productSearchScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSearch,
        enterTransition = {
            when (initialState.destination.route) {
                CatalogGraph.Catalog.routeSchema,
                UnscopedDestinations.Products.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slidePopEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                CatalogGraph.Catalog.routeSchema,
                UnscopedDestinations.Products.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        ProductSearchScreen(
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
                }
            },
        )
    }
}

fun NavHostController.navigateToProductSearchScreen() {
    this.navigate(UnscopedDestinations.ProductSearch.route)
}
