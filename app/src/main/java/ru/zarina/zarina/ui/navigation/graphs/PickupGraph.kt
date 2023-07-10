package ru.zarina.zarina.ui.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.ui.navigation.destinations.Subscribe
import ru.zarina.zarina.ui.screens.pickup.details.DetailsScreen
import ru.zarina.zarina.ui.screens.pickup.root.PickupRootScreen
import ru.zarina.zarina.ui.screens.pickup.selectpickupcity.SelectPickupCityScreen
import ru.zarina.zarina.ui.screens.pickup.selectsize.SelectSizeScreen

fun NavGraphBuilder.pickupGraph(navController: NavController) {
    navigationGraph(Pickup) {
        composableDestination(Pickup.Root) {
            val parentEntry =
                remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
            PickupRootScreen(
                parentEntry = parentEntry,
                showSelectSize = {
                    navController.navigate(Pickup.SelectSize.route)
                },
                showSelectCity = {
                    navController.navigate(Pickup.SelectCity.route)
                },
                showDetails = {
                    navController.navigate(Pickup.Details.route)
                },
                goBack = {
                    navController.popBackStack(Pickup.routeSchema, true)
                }
            )
        }
        bottomSheetDestination(Pickup.SelectSize) {
            val parentEntry =
                remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
            SelectSizeScreen(
                parentEntry = parentEntry,
                showSubscribe = { barcode ->
                    navController.navigate(Subscribe.createRoute(Subscribe.Arguments(barcode))) {
                        popUpTo(Pickup.SelectSize.routeSchema) {
                            inclusive = true
                        }
                    }
                },
                goBack = {
                    navController.popBackStack(Pickup.SelectSize.routeSchema, true)
                }
            )
        }
        composableDestination(Pickup.SelectCity) {
            val parentEntry =
                remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
            SelectPickupCityScreen(
                parentEntry = parentEntry,
                goBack = {
                    navController.popBackStack(Pickup.SelectCity.routeSchema, true)
                }
            )
        }
        composableDestination(Pickup.Details) {
            val parentEntry =
                remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
            DetailsScreen(
                parentEntry = parentEntry,
                showSuccess = {
                    navController.navigate(Pickup.Success.routeSchema) {
                        popUpTo(Pickup.Root.routeSchema) {
                            inclusive = true
                        }
                    }
                },
                goBack = {
                    navController.popBackStack(Pickup.Details.routeSchema, true)
                }
            )
        }
        composableDestination(Pickup.Success) {
            val parentEntry =
                remember(it) { navController.getBackStackEntry(Pickup.routeSchema) }
            ru.zarina.zarina.ui.screens.pickup.success.SuccessScreen(
                parentEntry = parentEntry,
                goBack = {
                    navController.popBackStack(Pickup.routeSchema, true)
                }
            )
        }
    }
}
