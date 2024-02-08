package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenAction
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenResult
import timber.log.Timber

fun NavGraphBuilder.sizeSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.SizeSelector) {
        SizeSelectorBottomSheetScreen(
            navigateForward = { action ->
                when (action) {
                    is SizeSelectorScreenAction.SizeClicked -> {
                        if (action.offers.size > 1) {
                            val args = UnscopedDestinations.HeightSelector.Args(
                                product = action.product,
                                offers = action.offers,
                            )
                            val route = UnscopedDestinations.HeightSelector.createRoute(args)
                            navController.navigate(route)
                        } else {
                            val offer = action.offers.firstOrNull()
                            if (offer != null) {
                                val args = UnscopedDestinations.ProductSubscription.Args(
                                    product = action.product,
                                    offer = offer,
                                )
                                val route = UnscopedDestinations.ProductSubscription.createRoute(args)
                                val navOptions = navOptions {
                                    popUpTo(UnscopedDestinations.SizeSelector.routeSchema) {
                                        inclusive = true
                                    }
                                }
                                navController.navigate(route, navOptions)
                            } else {
                                Timber.e("Could not navigate to ProductSubscription because offer is null")
                            }
                        }
                    }
                }
            },
            navigateBackward = { result ->
                when (result) {
                    SizeSelectorScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.SizeSelector.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
