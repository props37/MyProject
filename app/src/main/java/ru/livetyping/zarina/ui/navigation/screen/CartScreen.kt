package ru.livetyping.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.ui.navigation.util.slideExitTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.ui.screen.cart.CartScreen
import ru.livetyping.zarina.ui.screen.cart.CartScreenAction
import ru.livetyping.zarina.ui.screen.cart.CartViewModel

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
                    CartScreenAction.GoToCatalogClicked -> navController.navigateToCatalogScreen()
                    is CartScreenAction.CityClicked -> {
                        navController.navigateToCitySelectorScreen(
                            currentCity = action.currentCity,
                            title = Text.Resource(R.string.city_change),
                        )
                    }

                    is CartScreenAction.ProductCountClicked -> {
                        navController.navigateToProductCountSelector(
                            productId = action.productId,
                            barcode = action.barcode,
                            initialCount = action.initialCount,
                            availableCount = action.availableCount,
                            deliveryType = action.deliveryType,
                        )
                    }
                }
            },
        )
    }
}
