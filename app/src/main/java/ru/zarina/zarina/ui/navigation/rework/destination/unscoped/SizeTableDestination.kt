package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizetable.SizeTableBottomSheetScreen
import ru.zarina.zarina.ui.screen.sizetable.SizeTableScreenResult

fun NavGraphBuilder.sizeTableBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.SizeTable) {
        SizeTableBottomSheetScreen(
            navigateBackward = { result ->
                when (result) {
                    SizeTableScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.SizeTable.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
