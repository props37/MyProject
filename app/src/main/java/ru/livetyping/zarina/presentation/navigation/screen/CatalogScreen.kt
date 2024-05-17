package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CatalogGraph
import ru.livetyping.zarina.presentation.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreen
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreenAction

fun NavGraphBuilder.catalogScreen(
    navController: NavHostController,
) {
    composableDestination(
        destination = CatalogGraph.Catalog,
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ProductSearch.routeSchema,
                UnscopedDestinations.Products.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ProductSearch.routeSchema,
                UnscopedDestinations.Products.routeSchema -> slidePopEnterTransition()

                else -> null
            }
        },
    ) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        CatalogScreen(
            navigate = { action ->
                when (action) {
                    CatalogScreenAction.SearchClicked -> {
                        navController.navigateToProductSearchScreen()
                    }

                    is CatalogScreenAction.CategoryClicked -> {
                        navController.navigateToProductsScreen(action.categoryId)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCatalogScreen() {
    this.navigateToBottomNavBarItem(BottomNavBarItem.Catalog)
    this.popBackStack(
        route = CatalogGraph.Catalog.routeSchema,
        inclusive = false,
    )
}
