package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorBottomSheetScreenScreen
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorScreenAction

fun NavGraphBuilder.heightSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.HeightSelector) {
        HeightSelectorBottomSheetScreenScreen(
            navigate = { action ->
                when (action) {
                    HeightSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.HeightSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    HeightSelectorScreenAction.SizeSelectorFlowClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.SizeSelector.routeSchema,
                            inclusive = true,
                        )
                    }

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
        )
    }
}
