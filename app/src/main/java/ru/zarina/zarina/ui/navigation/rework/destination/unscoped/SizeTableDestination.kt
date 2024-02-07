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
                        // TODO: [High] Implement
                        val args = UnscopedDestinations.HeightSelector.Args(action.offers)
                        val route = UnscopedDestinations.HeightSelector.createRoute(args)
                        navController.navigate(route)
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
