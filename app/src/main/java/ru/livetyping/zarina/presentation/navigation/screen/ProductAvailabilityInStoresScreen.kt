package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresScreen
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresScreenAction

fun NavGraphBuilder.productAvailabilityInStoresScreen(navController: NavHostController) {
    composable<UnscopedDestinations.ProductAvailabilityInStores>(
        typeMap = UnscopedDestinations.ProductAvailabilityInStores.typeMap(),
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Product.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Product.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ProductAvailabilityInStoresScreen(
            navigate = { action ->
                when (action) {
                    ProductAvailabilityInStoresScreenAction.BackClicked -> navController.navigateUp()
                }
            },
        )
    }
}
