package ru.zarina.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.bottomnavbar.BottomNavBarItem
import ru.zarina.zarina.ui.bottomnavbar.navigateToBottomNavBarItem
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.navigation.destination.graph.CatalogGraph
import ru.zarina.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.navigation.util.slideExitTransition
import ru.zarina.zarina.ui.navigation.util.slidePopEnterTransition
import ru.zarina.zarina.ui.screen.cart.CartScreen
import ru.zarina.zarina.ui.screen.cart.CartScreenAction
import ru.zarina.zarina.ui.screen.cart.CartViewModel
import ru.zarina.zarina.util.library.navigation.navigate

fun NavGraphBuilder.cartScreen(navController: NavHostController) {
    composableDestination(
        destination = CartGraph.Cart,
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.CitySelector.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.CitySelector.routeSchema -> slidePopEnterTransition()
                else -> null
            }
        },
    ) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        CartScreen(
            viewModel = hiltViewModel { factory: CartViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    CartScreenAction.GoToCatalogClicked -> {
                        navController.navigateToBottomNavBarItem(BottomNavBarItem.Catalog)
                        navController.popBackStack(
                            route = CatalogGraph.Catalog.routeSchema,
                            inclusive = false,
                        )
                    }

                    is CartScreenAction.CityClicked -> {
                        val args = UnscopedDestinations.CitySelector.Args(
                            city = action.city,
                            title = Text.Resource(R.string.city_change),
                        )
                        navController.navigate(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            args = UnscopedDestinations.CitySelector.createArgsBundle(args),
                        )
                    }

                    is CartScreenAction.ProductCountClicked -> {
                        val args = CartGraph.ProductCountSelector.Args(
                            productId = action.productId,
                            barcode = action.barcode,
                            initialCount = action.initialCount,
                            availableCount = action.availableCount,
                        )
                        navController.navigate(
                            route = CartGraph.ProductCountSelector.routeSchema,
                            args = CartGraph.ProductCountSelector.createArgsBundle(args),
                        )
                    }
                }
            },
        )
    }
}
