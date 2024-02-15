package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorBottomSheetScreenScreen
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorScreenAction
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorScreenResult

fun NavGraphBuilder.heightSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.HeightSelector) {
        HeightSelectorBottomSheetScreenScreen(
            navigateForward = { action ->
                when (action) {
                    is HeightSelectorScreenAction.OfferClicked -> {
                        if (action.offer.isAvailable) {
                            // TODO: [High] Implement
                        } else {
                            val args = UnscopedDestinations.ProductSubscription.Args(
                                product = action.product,
                                offer = action.offer,
                            )
                            val route = UnscopedDestinations.ProductSubscription.createRoute(args)
                            val navOptions = navOptions {
                                popUpTo(UnscopedDestinations.HeightSelector.routeSchema) {
                                    inclusive = true
                                }
                            }
                            navController.navigate(route, navOptions)
                        }
                    }
                }
            },
            navigateBackward = { result ->
                when (result) {
                    HeightSelectorScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.HeightSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    HeightSelectorScreenResult.SizeSelectorFlowClosed -> {
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
