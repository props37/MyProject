package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.screen.cart.CartScreen
import ru.livetyping.zarina.presentation.screen.cart.CartScreenAction
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel

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
                val citySelectorResultFlow = it.savedStateHandle
                    .getStateFlow<UnscopedDestinations.CitySelector.Result?>(
                        key = UnscopedDestinations.CitySelector.RESULT_KEY,
                        initialValue = null,
                    )
                val productCountSelectorResultFlow = it.savedStateHandle
                    .getStateFlow<CartGraph.ProductCountSelector.Result?>(
                        key = CartGraph.ProductCountSelector.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(citySelectorResultFlow, productCountSelectorResultFlow)
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
