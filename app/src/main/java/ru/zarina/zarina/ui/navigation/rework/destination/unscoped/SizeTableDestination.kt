package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenAction
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenResult

fun NavGraphBuilder.sizeSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.SizeSelector) {
        SizeSelectorBottomSheetScreen(
            navigateForward = { action ->
                when (action) {
                    is SizeSelectorScreenAction.SizeClicked -> {
                        val route = if (action.offers.size > 1) {
                            val args = UnscopedDestinations.HeightSelector.Args(
                                product = action.product,
                                offers = action.offers,
                            )
                            UnscopedDestinations.HeightSelector.createRoute(args)
                        } else {
                            // TODO: [High] Navigate to subscription screen
                            null
                        }
                        if (route != null) navController.navigate(route)
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
